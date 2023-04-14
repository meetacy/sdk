package app.meetacy.sdk.engine.ktor.requests.users

import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.requests.GetMeRequest
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.types.exception.meetacyApiError
import app.meetacy.sdk.types.user.SelfUser
import dev.icerock.moko.network.generated.apis.UserApi
import dev.icerock.moko.network.generated.apis.UserApiImpl
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.GetUserRequest as GeneratedGetUserRequest

internal class UsersEngine(
    baseUrl: String,
    httpClient: HttpClient,
    json: Json
) {
    private val base: UserApi = UserApiImpl(baseUrl, httpClient, json)

    suspend fun getMe(request: GetMeRequest): GetMeRequest.Response {
        val response = base.usersGetPost(
            getUserRequest = GeneratedGetUserRequest(
                token = request.token.string
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        return GetMeRequest.Response(
            (response.result?.mapToUser() ?: meetacyApiError("'result' should present"))
                    as? SelfUser ?: meetacyApiError("Resulted user is not SelfUser")
        )
    }

    suspend fun getUser(request: GetUserRequest): GetUserRequest.Response {
        val response = base.usersGetPost(
            getUserRequest = GeneratedGetUserRequest(
                token = request.token.string,
                id = request.userId.string,
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        return GetUserRequest.Response(
            response.result?.mapToUser() ?: meetacyApiError("'result' should present")
        )
    }
}
