package app.meetacy.sdk.requests.auth

import app.meetacy.sdk.internal.Response
import app.meetacy.sdk.internal.meetacyPost
import app.meetacy.sdk.model.AccessIdentity
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

@Serializable
private data class GenerateTokenBody(val nickname: String)

internal suspend fun HttpClient.generateToken(nickname: String): AccessIdentity {
    val response = meetacyPost<AccessIdentity>(urlString = "/auth/generate") {
        setBody(GenerateTokenBody(nickname))
    }
    response as Response.Success
    return response.result
}
