@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.auth

import app.meetacy.sdk.engine.requests.GenerateAuthRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import dev.icerock.moko.network.generated.apis.AuthApi
import dev.icerock.moko.network.generated.apis.AuthApiImpl
import dev.icerock.moko.network.generated.models.GenerateIdentityRequest
import io.ktor.client.*
import kotlinx.serialization.json.Json

internal class AuthEngine(
    baseUrl: String,
    httpClient: HttpClient,
    json: Json
) {
    private val base: AuthApi = AuthApiImpl(baseUrl, httpClient, json)

    suspend fun generate(request: GenerateAuthRequest): GenerateAuthRequest.Response {
        val response = base.authGeneratePost(
            generateIdentityRequest = GenerateIdentityRequest(
                nickname = request.nickname
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        return GenerateAuthRequest.Response(token = Token(response.result))
    }
}
