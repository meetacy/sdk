package app.meetacy.sdk.engine.ktor.requests.users

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.mapToSelfUser
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.requests.extencion.postWithoutToken
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.EditUserRequest
import app.meetacy.sdk.engine.requests.GetMeRequest
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.engine.requests.UsernameAvailableRequest
import app.meetacy.sdk.exception.meetacyApiError
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.optional.map
import app.meetacy.sdk.types.serializable.file.FileIdSerializable
import app.meetacy.sdk.types.serializable.file.serializable
import app.meetacy.sdk.types.serializable.optional.OptionalSerializable
import app.meetacy.sdk.types.serializable.optional.serializable
import app.meetacy.sdk.types.serializable.user.*
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.Username
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders.Authorization
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import app.meetacy.sdk.engine.ktor.models.GetUserRequest as ModelGetUserRequest

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

        val response = json.decodeFromString<app.meetacy.sdk.engine.ktor.models.GetUserResponse>(string)

        return GetMeRequest.Response(
            (response.result?.mapToUser() ?: meetacyApiError("'result' should present"))
                    as? SelfUser ?: meetacyApiError("Resulted user is not SelfUser")
        )
    }

    @Serializable
    private data class GetUserBody(val id: UserIdSerializable)

    private fun GetUserRequest.toBody() = GetUserBody(userId.serializable())

    suspend fun getUser(request: GetUserRequest): GetUserRequest.Response {
        val url = baseUrl / "get"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<UserSerializable>()
        return GetUserRequest.Response(response.type())
    }

    @Serializable
    private data class EditUserBody(
        val nickname: OptionalSerializable<String>,
        val avatarId: OptionalSerializable<FileIdSerializable?>,
        val username: OptionalSerializable<UsernameSerializable?>
    )

    private fun EditUserRequest.toBody() = EditUserBody(
        nickname = nickname.serializable(),
        avatarId = avatarId.map { it?.serializable() }.serializable(),
        username = username.map { it?.serializable() }.serializable()
    )

    suspend fun editUser(request: EditUserRequest): EditUserRequest.Response {
        val url = baseUrl / "edit"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<UserSerializable>()
        val result = response.type() as SelfUser
        return EditUserRequest.Response(result)
    }

    @OptIn(UnsafeConstructor::class)
    suspend fun usernameAvailable(request: UsernameAvailableRequest): UsernameAvailableRequest.Response {
        val url = baseUrl / "username" / "available"

        val jsonObject = buildJsonObject {
            put("username", request.username.string)
        }

        val string = postWithoutToken(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<app.meetacy.sdk.engine.ktor.models.ValidateUsernameRequest>(string).username

        return UsernameAvailableRequest.Response(username = Username(response))
    }
}
