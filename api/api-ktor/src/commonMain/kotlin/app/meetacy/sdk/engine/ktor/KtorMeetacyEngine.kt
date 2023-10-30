package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.engine.MeetacyRequestsEngine
import app.meetacy.sdk.engine.ktor.exception.getException
import app.meetacy.sdk.engine.ktor.requests.auth.AuthEngine
import app.meetacy.sdk.engine.ktor.requests.files.FilesEngine
import app.meetacy.sdk.engine.ktor.requests.friends.FriendsEngine
import app.meetacy.sdk.engine.ktor.requests.invitations.InvitationsEngine
import app.meetacy.sdk.engine.ktor.requests.meetings.MeetingsEngine
import app.meetacy.sdk.engine.ktor.requests.notifications.NotificationsEngine
import app.meetacy.sdk.engine.ktor.requests.search.SearchEngine
import app.meetacy.sdk.engine.ktor.requests.updates.UpdatesEngine
import app.meetacy.sdk.engine.ktor.requests.users.UsersEngine
import app.meetacy.sdk.engine.ktor.response.ServerResponse
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.exception.MeetacyConnectionException
import app.meetacy.sdk.exception.MeetacyException
import app.meetacy.sdk.exception.MeetacyInternalException
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.url.parametersOf
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import io.rsocket.kotlin.ktor.client.RSocketSupport
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json

public class KtorMeetacyEngine(
    private val baseUrl: Url,
    httpClient: HttpClient = HttpClient(),
    json: Json = Json,
) : MeetacyRequestsEngine {

    private val json = Json(json) {
        ignoreUnknownKeys = true
    }

    private val httpClient = httpClient.config {
        expectSuccess = true

        install(WebSockets)
        install(RSocketSupport)
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
        }
    }

    private val auth = AuthEngine(baseUrl, this.httpClient)
    private val users = UsersEngine(baseUrl, this.httpClient)
    private val friends = FriendsEngine(baseUrl, this.httpClient, this.json)
    private val meetings = MeetingsEngine(baseUrl, this.httpClient)
    private val files = FilesEngine(baseUrl, this.httpClient)
    private val invitations = InvitationsEngine(baseUrl, this.httpClient)
    private val notifications = NotificationsEngine(baseUrl, this.httpClient)
    private val search = SearchEngine(baseUrl, this.httpClient)
    private val updates = UpdatesEngine(baseUrl, this.httpClient, this.json)

    override fun getFileUrl(
        id: FileId
    ): Url = baseUrl / "files" / "download" + parametersOf("fileId" to id.string)

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> execute(request: MeetacyRequest<T>): T = handleMeetacyExceptions {
        return when (request) {
            // auth
            is GenerateAuthRequest -> auth.generate(request) as T
            // friends
            is AddFriendRequest -> friends.add(request) as T
            is DeleteFriendRequest -> friends.delete(request) as T
            is ListFriendsRequest -> friends.list(request) as T
            is EmitFriendsLocationRequest -> friends.streamFriendsLocation(request) as T
            // users
            is GetMeRequest -> users.getMe(request) as T
            is GetUserRequest -> users.getUser(request) as T
            is EditUserRequest -> users.editUser(request) as T
            is UsernameAvailableRequest -> users.usernameAvailable(request) as T
            // meetings
            is ListMeetingsHistoryRequest -> meetings.listMeetingsHistory(request) as T
            is ListActiveMeetingsRequest -> meetings.listActiveMeetings(request) as T
            is ListPastMeetingsRequest -> meetings.listPastMeetings(request) as T
            is ListMeetingsMapRequest -> meetings.listMeetingsMap(request) as T
            is CreateMeetingRequest -> meetings.createMeeting(request) as T
            is ParticipateMeetingRequest -> meetings.participateMeeting(request) as T
            is GetMeetingRequest -> meetings.getMeeting(request) as T
            is EditMeetingRequest -> meetings.editMeeting(request) as T
            is ListMeetingParticipantsRequest -> meetings.listMeetingParticipants(request) as T
            // files
            is GetFileRequest -> files.get(request) as T
            is UploadFileRequest -> files.upload(request) as T
            // invitations
            is CreateInvitationRequest -> invitations.create(request) as T
            is AcceptInvitationRequest -> invitations.accept(request) as T
            is DenyInvitationRequest -> invitations.deny(request) as T
            is CancelInvitationRequest -> invitations.cancel(request) as T
            // notifications
            is ReadNotificationRequest -> notifications.read(request) as T
            is ListNotificationsRequest -> notifications.list(request) as T
            // search
            is SearchRequest -> search.search(request) as T
            // updates
            is EmitUpdatesRequest -> updates.stream(request) as T
            // not yet supported
            is LinkEmailRequest -> notSupported()
            is ConfirmEmailRequest -> notSupported()
        }
    }

    private suspend inline fun <T> handleMeetacyExceptions(block: () -> T): T {
        return try {
            block()
        } catch (exception: ResponseException) {
            val response = try {
                json.decodeFromString<ServerResponse.Error>(exception.response.body())
            } catch (exception: Throwable) {
                throw MeetacyInternalException(cause = exception)
            }
            throw getException(response)
        } catch (exception: IOException) {
            throw MeetacyConnectionException(cause = exception)
        } catch (exception: MeetacyException) {
            throw exception
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: RuntimeException) {
            throw MeetacyInternalException(cause = exception)
        }
    }

    private fun notSupported(): Nothing = TODO("This request is not supported yet!")
}
