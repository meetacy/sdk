package app.meetacy.sdk.engine.ktor.requests.users

import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.requests.EditUserRequest
import app.meetacy.sdk.engine.requests.GetMeRequest
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.types.exception.meetacyApiError
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.SelfUser
import dev.icerock.moko.network.generated.apis.UserApi
import dev.icerock.moko.network.generated.apis.UserApiImpl
import dev.icerock.moko.network.generated.models.EditUserResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import dev.icerock.moko.network.generated.models.GetUserRequest as GeneratedGetUserRequest

internal class UsersEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    json: Json
) {
    private val base: UserApi = UserApiImpl(baseUrl.string, httpClient, json)

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

    suspend fun editUser(request: EditUserRequest): EditUserRequest.Response = with(request) {
        val url = baseUrl / "users" / "edit"

        val jsonObject = buildJsonObject {
            put("token", token.string)

            nickname.ifPresent { nickname ->
                put("nickname", nickname)
            }
            avatarId.ifPresent { avatarId ->
                put("avatarId", avatarId?.string)
            }
        }

        val string = httpClient.post(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
        }.body<String>()

        val user = Json.decodeFromString<EditUserResponse>(string).result

        return EditUserRequest.Response(user = user.mapToUser() as SelfUser)
    }
}
