package app.meetacy.sdk.engine.ktor.requests.users

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.EditUserRequest
import app.meetacy.sdk.engine.requests.GetMeRequest
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.engine.requests.UsernameAvailableRequest
import app.meetacy.sdk.types.optional.map
import app.meetacy.sdk.types.serializable.file.FileIdSerializable
import app.meetacy.sdk.types.serializable.file.serializable
import app.meetacy.sdk.types.serializable.optional.OptionalSerializable
import app.meetacy.sdk.types.serializable.optional.serializable
import app.meetacy.sdk.types.serializable.user.*
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.SelfUser
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

internal class UsersEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    private val baseUrl = baseUrl / "users"

    private fun toBody() = GetUserBody(null)

    suspend fun getMe(request: GetMeRequest): GetMeRequest.Response {
        val url = baseUrl / "get"
        val body = toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<UserSerializable>()
        return GetMeRequest.Response(response.type() as SelfUser)
    }

    @Serializable
    private data class GetUserBody(val id: UserIdSerializable?)
    private fun GetUserRequest.toBody() = GetUserBody(userId?.serializable())

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
        val nickname: OptionalSerializable<String> = OptionalSerializable.Undefined,
        val avatarId: OptionalSerializable<FileIdSerializable?> = OptionalSerializable.Undefined,
        val username: OptionalSerializable<UsernameSerializable?> = OptionalSerializable.Undefined
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

    @Serializable
    private data class UsernameAvailableBody(val username: UsernameSerializable?)

    private fun UsernameAvailableRequest.toBody() = UsernameAvailableBody(
        username = username.serializable()
    )

    suspend fun usernameAvailable(request: UsernameAvailableRequest): UsernameAvailableRequest.Response {
        val url = baseUrl / "username" / "available"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            setBody(body)
        }.bodyAsSuccess<UsernameSerializable>()

        return UsernameAvailableRequest.Response(response.type())
    }
}
