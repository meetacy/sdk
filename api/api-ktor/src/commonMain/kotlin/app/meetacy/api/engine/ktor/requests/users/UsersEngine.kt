@file:OptIn(UnsafeConstructor::class)

package app.meetacy.api.engine.ktor.requests.users

import app.meetacy.api.engine.requests.GetMeRequest
import app.meetacy.api.engine.requests.GetUserRequest
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.email.Email
import app.meetacy.types.exception.meetacyApiError
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.SelfUser
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId
import dev.icerock.moko.network.generated.apis.UserApi
import dev.icerock.moko.network.generated.apis.UserApiImpl
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.GetUserRequest as GeneratedGetUserRequest
import dev.icerock.moko.network.generated.models.User as GeneratedUser

internal class UsersEngine(
    baseUrl: String,
    httpClient: HttpClient,
    json: Json
) {
    private val base: UserApi = UserApiImpl(baseUrl, httpClient, json)

    suspend fun getMe(request: GetMeRequest): GetMeRequest.Response {
        val response = base.usersGetPost(
            getUserRequest = GeneratedGetUserRequest(
                accessIdentity = request.token.string
            )
        )

        return GetMeRequest.Response(
            (response.result?.mapToUser() ?: meetacyApiError("'result' should present"))
                    as? SelfUser ?: meetacyApiError("Resulted user is not SelfUser")
        )
    }

    suspend fun getUser(request: GetUserRequest): GetUserRequest.Response {
        val response = base.usersGetPost(
            getUserRequest = GeneratedGetUserRequest(
                accessIdentity = request.token.string,
                identity = request.userId.string,
            )
        )

        return GetUserRequest.Response(
            response.result?.mapToUser() ?: meetacyApiError("'result' should present")
        )
    }

    private fun GeneratedUser.mapToUser(): User = if (isSelf) {
        SelfUser(
            id = UserId(identity),
            nickname = nickname,
            email = email?.let(::Email),
            emailVerified = emailVerified ?: error("Self user must always return emailVerified parameter")
        )
    } else {
        RegularUser(
            id = UserId(identity),
            nickname = nickname
        )
    }
}
