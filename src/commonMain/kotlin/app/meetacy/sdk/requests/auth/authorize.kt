package app.meetacy.sdk.requests.auth

import app.meetacy.sdk.authorized.AuthorizedClient
import app.meetacy.sdk.exception.InvalidTokenException
import app.meetacy.sdk.model.AccessIdentity
import app.meetacy.sdk.requests.users.getMe
import io.ktor.client.*

sealed interface AuthorizeResult {
    data class Success(val client: AuthorizedClient) : AuthorizeResult
    object Failure : AuthorizeResult
}

internal suspend fun HttpClient.authorize(accessIdentity: AccessIdentity): AuthorizeResult {
    return try {
        val user = getMe(accessIdentity)
        AuthorizeResult.Success(AuthorizedClient(user, accessIdentity, httpClient = this))
    } catch (_: InvalidTokenException) {
        AuthorizeResult.Failure
    }
}
