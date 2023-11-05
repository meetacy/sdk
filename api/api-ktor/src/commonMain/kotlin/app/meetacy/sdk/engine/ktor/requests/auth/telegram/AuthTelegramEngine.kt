@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.auth.telegram

import app.meetacy.sdk.engine.requests.AwaitTelegramAuthRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class AuthTelegramEngine(
    baseUrl: Url,
    private val rsocketClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = baseUrl / "telegram"

    @Serializable
    private data class AwaitTelegramAuthInit(
        val temporalToken: String,
        val apiVersion: Int
    )

    @Serializable
    private data class AwaitTelegramAuthResult(val token: String)


    suspend fun await(
        request: AwaitTelegramAuthRequest
    ): AwaitTelegramAuthRequest.Response {
        val url = baseUrl / "await"

        val socket = rsocketClient.rSocket(
            urlString = url.string,
            secure = url.protocol.isWss
        )

        val requestPayload = buildPayload {
            data(json.encodeToString(request))
        }

        val responsePayload = socket.requestResponse(requestPayload)
        val deserialized = json.decodeFromString<AwaitTelegramAuthResult>(responsePayload.data.readText())

        return AwaitTelegramAuthRequest.Response(token = Token(deserialized.token))
    }
}
