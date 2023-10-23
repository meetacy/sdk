package app.meetacy.sdk.engine.ktor.requests.users

import app.meetacy.sdk.engine.ktor.mapToSelfUser
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.requests.extencion.postWithoutToken
import app.meetacy.sdk.engine.ktor.response.models.GetUserResponse
import app.meetacy.sdk.engine.ktor.response.models.ValidateUsernameRequest
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
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import app.meetacy.sdk.engine.ktor.response.models.GetUserRequest as ModelGetUserRequest

internal class UsersEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = baseUrl / "users"

    suspend fun getMe(request: GetMeRequest): GetMeRequest.Response {
        val url = baseUrl / "get"

        val jsonObject = buildJsonObject {
            put("id", ModelGetUserRequest().id)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<GetUserResponse>(string)

        return GetMeRequest.Response(
            (response.result?.mapToUser() ?: meetacyApiError("'result' should present"))
                    as? SelfUser ?: meetacyApiError("Resulted user is not SelfUser")
        )
    }

    suspend fun getUser(request: GetUserRequest): GetUserRequest.Response {
        val url = baseUrl / "get"

        val jsonObject = buildJsonObject {
            put("id", ModelGetUserRequest(
                request.userId.string
            ).id)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<GetUserResponse>(string)

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

        val user = Json.decodeFromString<app.meetacy.sdk.engine.ktor.response.models.EditUserResponse>(string).result

        return EditUserRequest.Response(user = user.mapToSelfUser())
    }

    @OptIn(UnsafeConstructor::class)
    suspend fun usernameAvailable(request: UsernameAvailableRequest): UsernameAvailableRequest.Response {
        val url = baseUrl / "username" / "validate"

        val jsonObject = buildJsonObject {
            put("username", request.username.string)
        }

        val string = postWithoutToken(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<ValidateUsernameRequest>(string)

        return UsernameAvailableRequest.Response(username = Username(response.username))
    }
}
