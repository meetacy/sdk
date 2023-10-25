@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.auth

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.requests.GenerateAuthRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

internal class AuthEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient
) {
    @Serializable
    private data class GenerateAuthBody(val nickname: String)

    suspend fun generate(request: GenerateAuthRequest): GenerateAuthRequest.Response {
        val url = baseUrl / "auth" / "generate"
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
