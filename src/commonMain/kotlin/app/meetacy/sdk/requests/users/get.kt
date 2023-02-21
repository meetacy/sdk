package app.meetacy.sdk.requests.users

import app.meetacy.sdk.exception.InvalidTokenException
import app.meetacy.sdk.internal.Response
import app.meetacy.sdk.internal.meetacyPost
import app.meetacy.sdk.model.*
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

sealed interface GetUserResult {
    class Success(val user: User) : GetUserResult
    object NotFound : GetUserResult
}

@Serializable
private data class GetUserBody(
    val accessIdentity: AccessIdentity,
    val id: UserId?
)

internal suspend fun HttpClient.getUser(
    accessIdentity: AccessIdentity,
    id: UserId? = null
): GetUserResult {
    val response = meetacyPost<User>(urlString = "/users/get") {
        setBody(GetUserBody(accessIdentity, id))
    }
    return when (response) {
        is Response.Error -> when (response.errorCode) {
            1 -> throw InvalidTokenException()
            2 -> GetUserResult.NotFound
            else -> response.unknownErrorCode()
        }
        is Response.Success -> GetUserResult.Success(response.result)
    }
}

internal suspend fun HttpClient.getMe(accessIdentity: AccessIdentity): SelfUser {
    val result = getUser(accessIdentity) as GetUserResult.Success
    return result.user.toSelfUser()
}
