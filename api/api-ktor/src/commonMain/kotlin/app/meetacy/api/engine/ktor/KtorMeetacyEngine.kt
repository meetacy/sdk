@file:OptIn(UnsafeConstructor::class)

package app.meetacy.api.engine.ktor

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.ktor.requests.auth.AuthEngine
import app.meetacy.api.engine.ktor.requests.friends.FriendsEngine
import app.meetacy.api.engine.ktor.requests.users.UsersEngine
import app.meetacy.api.engine.requests.*
import app.meetacy.api.engine.updates.MeetacyUpdate
import app.meetacy.api.engine.updates.filter.MeetacyUpdateFilter
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.update.UpdateId
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json

public class KtorMeetacyEngine(
    baseUrl: String,
    httpClient: HttpClient = HttpClient(),
    json: Json = Json,
) : MeetacyRequestsEngine {

    private val auth = AuthEngine(baseUrl, httpClient, json)
    private val users = UsersEngine(baseUrl, httpClient, json)
    private val friends = FriendsEngine(baseUrl, httpClient, json)

    override fun updatesPolling(
        token: Token,
        vararg filters: MeetacyUpdateFilter<*>,
        lastUpdateId: UpdateId?,
    ): Flow<MeetacyUpdate> = emptyFlow() // TODO: create updates from socket

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> execute(request: MeetacyRequest<T>): T {
        return when (request) {
            // auth
            is GenerateAuthRequest -> auth.generate(request) as T
            // friends
            is AddFriendRequest -> friends.add(request) as T
            is DeleteFriendRequest -> friends.delete(request) as T
            is ListFriendsRequest -> friends.list(request) as T
            // users
            is GetMeRequest -> users.getMe(request) as T
            is GetUserRequest -> users.getUser(request) as T
            // not yet supported
            is LinkEmailRequest -> notSupported()
            is ConfirmEmailRequest -> notSupported()
        }
    }

    private fun notSupported(): Nothing = TODO("This request is not supported yet!")
}
