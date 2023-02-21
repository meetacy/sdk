package app.meetacy.sdk.authorized

import app.meetacy.sdk.model.AccessIdentity
import app.meetacy.sdk.model.SelfUser
import app.meetacy.sdk.model.UserId
import app.meetacy.sdk.requests.friends.getFriends
import app.meetacy.sdk.requests.meetings.getMeetings
import app.meetacy.sdk.requests.users.getMe
import app.meetacy.sdk.requests.users.getUser
import io.ktor.client.*

class AuthorizedClient(
    val user: SelfUser,
    private val accessIdentity: AccessIdentity,
    private val httpClient: HttpClient
) {
    suspend fun getUser(identity: UserId) = httpClient.getUser(accessIdentity, identity)
    suspend fun getMe() = httpClient.getMe(accessIdentity)
    suspend fun getMeetings() = httpClient.getMeetings(accessIdentity)
    suspend fun getFriends() = httpClient.getFriends(accessIdentity)
}
