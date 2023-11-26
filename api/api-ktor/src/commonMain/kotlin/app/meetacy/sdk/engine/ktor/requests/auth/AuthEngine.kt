@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.auth

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.requests.auth.telegram.AuthTelegramEngine
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.requests.GenerateAuthRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.url.Url
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class AuthEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    rsocketClient: HttpClient,
    json: Json
) {
    private val baseUrl: Url = baseUrl / "auth"

    val telegram: AuthTelegramEngine = AuthTelegramEngine(
        baseUrl = this.baseUrl,
        httpClient = httpClient,
        rsocketClient = rsocketClient,
        json = json
    )

    @Serializable
    private data class GenerateAuthBody(val nickname: String)

    suspend fun generate(request: GenerateAuthRequest): GenerateAuthRequest.Response {
        val url = baseUrl / "generate"
        val body = GenerateAuthBody(request.nickname)
        val response = httpClient
            .post(url.string) {
                apiVersion(request.apiVersion)
                setBody(body)
            }
            .bodyAsSuccess<String>()
        val result = Token(response)
        return GenerateAuthRequest.Response(result)
    }
}
