@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.auth

import app.meetacy.sdk.engine.ktor.response.models.GenerateIdentityResponse
import app.meetacy.sdk.engine.requests.GenerateAuthRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class AuthEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    suspend fun generate(request: GenerateAuthRequest): GenerateAuthRequest.Response {
        val url = baseUrl / "auth" / "generate"

        val jsonObject = buildJsonObject {
            put("nickname", request.nickname)
        }

        val string = httpClient.post(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
            header("Api-Version", request.apiVersion.int.toString())
        }.body<String>()

        val token = json.decodeFromString<GenerateIdentityResponse>(string).result

        return GenerateAuthRequest.Response(token = Token(token))
    }
}
