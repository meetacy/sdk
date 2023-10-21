package app.meetacy.sdk.engine.ktor.requests.users

import app.meetacy.sdk.engine.ktor.mapToSelfUser
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.requests.EditUserRequest
import app.meetacy.sdk.engine.requests.GetMeRequest
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.engine.requests.UsernameAvailableRequest
import app.meetacy.sdk.exception.meetacyApiError
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.Username
import dev.icerock.moko.network.generated.apis.UserApi
import dev.icerock.moko.network.generated.apis.UserApiImpl
import dev.icerock.moko.network.generated.models.EditUserResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import dev.icerock.moko.network.generated.models.GetUserRequest as GeneratedGetUserRequest
import dev.icerock.moko.network.generated.models.ValidateUsernameRequest as GeneratedValidateUsernameRequest

internal class UsersEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    json: Json
) {
    private val base: UserApi = UserApiImpl(baseUrl.string, httpClient, json)

    suspend fun getMe(request: GetMeRequest): GetMeRequest.Response {
        val response = base.usersGetPost(
            getUserRequest = GeneratedGetUserRequest(),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        return GetMeRequest.Response(
            (response.result?.mapToUser() ?: meetacyApiError("'result' should present"))
                    as? SelfUser ?: meetacyApiError("Resulted user is not SelfUser")
        )
    }

    suspend fun getUser(request: GetUserRequest): GetUserRequest.Response {
        val response = base.usersGetPost(
            getUserRequest = GeneratedGetUserRequest(
                id = request.userId.string,
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        return GetUserRequest.Response(
            response.result?.mapToUser() ?: meetacyApiError("'result' should present")
        )
    }

    suspend fun editUser(request: EditUserRequest): EditUserRequest.Response = with(request) {
        val url = baseUrl / "users" / "edit"

        val jsonObject = buildJsonObject {
            nickname.ifPresent { nickname ->
                put("nickname", nickname)
            }
            avatarId.ifPresent { avatarId ->
                put("avatarId", avatarId?.string)
            }
            username.ifPresent { username ->
                put("username", username?.string)
            }
        }

        val string = httpClient.post(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
            header("Authorization", token.string)
            header("Api-Version", apiVersion.int.toString())
        }.body<String>()

        val user = Json.decodeFromString<EditUserResponse>(string).result

        return EditUserRequest.Response(user = user.mapToSelfUser())
    }

    @OptIn(UnsafeConstructor::class)
    suspend fun usernameAvailable(request: UsernameAvailableRequest): UsernameAvailableRequest.Response {
        val response = base.usersValidatePost(
            validateUsernameRequest = GeneratedValidateUsernameRequest(
                username = request.username.string
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        return UsernameAvailableRequest.Response(username = Username(response.username))
    }
}
