package app.meetacy.sdk.requests.friends

import app.meetacy.sdk.internal.Response
import app.meetacy.sdk.internal.meetacyPost
import app.meetacy.sdk.model.AccessIdentity
import app.meetacy.sdk.model.User
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

@Serializable
private data class GetFriendsBody(val accessIdentity: AccessIdentity)

internal suspend fun HttpClient.getFriends(accessIdentity: AccessIdentity): List<User> {
    val result = meetacyPost<List<User>>(urlString = "/friends/get") {
        setBody(GetFriendsBody(accessIdentity))
    }
    when (result) {
        is Response.Error -> TODO()
        is Response.Success -> TODO()
    }
}
