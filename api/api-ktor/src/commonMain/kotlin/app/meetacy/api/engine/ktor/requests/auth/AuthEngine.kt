@file:OptIn(UnsafeConstructor::class)

package app.meetacy.api.engine.ktor.requests.auth

import app.meetacy.api.engine.requests.GenerateAuthRequest
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
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
            )
        )

        return GenerateAuthRequest.Response(token = Token(response.result))
    }
}
