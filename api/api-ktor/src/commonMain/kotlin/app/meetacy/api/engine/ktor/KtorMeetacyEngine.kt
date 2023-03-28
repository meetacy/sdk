package app.meetacy.api.engine.ktor

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.ktor.requests.auth.AuthEngine
import app.meetacy.api.engine.ktor.requests.friends.FriendsEngine
import app.meetacy.api.engine.ktor.requests.meetings.MeetingsEngine
import app.meetacy.api.engine.ktor.requests.users.UsersEngine
import app.meetacy.api.engine.requests.*
import io.ktor.client.*
import kotlinx.serialization.json.Json

public class KtorMeetacyEngine(
    baseUrl: String,
    httpClient: HttpClient = HttpClient(),
    json: Json = Json,
) : MeetacyRequestsEngine {

    private val auth = AuthEngine(baseUrl, httpClient, json)
    private val users = UsersEngine(baseUrl, httpClient, json)
    private val friends = FriendsEngine(baseUrl, httpClient, json)
    private val meetings = MeetingsEngine(baseUrl, httpClient, json)

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
            // meetings
            is ListMeetingsHistoryRequest -> meetings.listMeetingsHistory(request) as T
            is CreateMeetingRequest -> meetings.createMeeting(request) as T
            is ParticipateMeetingRequest -> meetings.participateMeeting(request) as T
            // not yet supported
            is LinkEmailRequest -> notSupported()
            is ConfirmEmailRequest -> notSupported()
        }
    }

    private fun notSupported(): Nothing = TODO("This request is not supported yet!")
}
