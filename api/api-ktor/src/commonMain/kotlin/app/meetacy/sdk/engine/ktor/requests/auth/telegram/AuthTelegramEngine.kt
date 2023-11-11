@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.auth.telegram

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.handleRSocketExceptions
import app.meetacy.sdk.engine.ktor.response.ServerResponse
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.requests.AwaitTelegramAuthRequest
import app.meetacy.sdk.engine.requests.FinishTelegramAuthRequest
import app.meetacy.sdk.engine.requests.PreloginTelegramAuthRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.auth.telegram.TempTelegramAuth
import app.meetacy.sdk.types.url.Url
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class AuthTelegramEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    private val rsocketClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = baseUrl / "telegram"

    @Serializable
    private data class AwaitTelegramAuthInit(
        val temporalToken: String,
        val apiVersion: Int
    )

    suspend fun await(
        request: AwaitTelegramAuthRequest
    ): AwaitTelegramAuthRequest.Response = handleRSocketExceptions(json) {
        val url = baseUrl.replaceProtocolWithWebsocket() / "await"

        val socket = rsocketClient.rSocket(
            urlString = url.string,
            secure = url.protocol.isWss
        )

        val serializable = with(request) {
            AwaitTelegramAuthInit(
                temporalToken = temporalToken.string,
                apiVersion = apiVersion.int
            )
        }
        val requestPayload = buildPayload {
            data(json.encodeToString(serializable))
        }

        val responsePayload = socket.requestResponse(requestPayload)
        val deserialized = json.decodeFromString<ServerResponse<String>>(
            string = responsePayload.data.readText()
        ) as ServerResponse.Success

        return AwaitTelegramAuthRequest.Response(
            permanentToken = Token(deserialized.result)
        )
    }

    @Serializable
    private data class FinishBody(
        val temporalHash: String,
        val secretBotKey: String,
        val telegramId: Long,
        val username: String?,
        val firstName: String?,
        val lastName: String?,
    )

    suspend fun finish(
        request: FinishTelegramAuthRequest
    ) {
        val url = baseUrl / "finish"
        httpClient
            .get(url.string) {
                apiVersion(request.apiVersion)
                setBody(
                    FinishBody(
                        temporalHash = request.temporalHash.string,
                        secretBotKey = request.secretBotKey.string,
                        telegramId = request.telegramId,
                        username = request.username,
                        firstName = request.firstName,
                        lastName = request.lastName,
                    )
                )
            }
            .bodyAsSuccess<StatusTrueResponse>()
    }

    @Serializable
    private data class PreloginResponse(
        val token: String,
        val botLink: String
    )

    suspend fun prelogin(
        request: PreloginTelegramAuthRequest
    ): PreloginTelegramAuthRequest.Response {
        val url = baseUrl / "prelogin"
        val response = httpClient
            .get(url.string) { apiVersion(request.apiVersion) }
            .bodyAsSuccess<PreloginResponse>()
        return PreloginTelegramAuthRequest.Response(
            tempAuth = TempTelegramAuth(
                token = Token(response.token),
                botLink = Url(response.botLink)
            )
        )
    }
}
