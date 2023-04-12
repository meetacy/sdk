package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.engine.MeetacyRequestsEngine
import app.meetacy.sdk.engine.ktor.requests.auth.AuthEngine
import app.meetacy.sdk.engine.ktor.requests.friends.FriendsEngine
import app.meetacy.sdk.engine.ktor.requests.meetings.MeetingsEngine
import app.meetacy.sdk.engine.ktor.requests.users.UsersEngine
import app.meetacy.sdk.engine.ktor.response.ErrorResponse
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.exception.MeetacyUnauthorizedException
import app.meetacy.sdk.exception.MeetacyConnectionException
import app.meetacy.sdk.exception.MeetacyInternalException
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.url.parametersOf
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

public class KtorMeetacyEngine(
    private val baseUrl: String,
    httpClient: HttpClient = HttpClient(),
    json: Json = Json,
) : MeetacyRequestsEngine {

    private val httpClient = httpClient.config {
        expectSuccess = true
    }

    private val auth = AuthEngine(baseUrl, this.httpClient, json)
    private val users = UsersEngine(baseUrl, this.httpClient, json)
    private val friends = FriendsEngine(baseUrl, this.httpClient, json)
    private val meetings = MeetingsEngine(baseUrl, this.httpClient, json)

    override fun getFileUrl(
        id: FileId
    ): Url = Url(baseUrl) / "files" / "download" + parametersOf("fileIdentity" to id.string)

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> execute(request: MeetacyRequest<T>): T = handleMeetacyExceptions {
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
            is ListMeetingsMapRequest -> meetings.listMeetingsMap(request) as T
            is CreateMeetingRequest -> meetings.createMeeting(request) as T
            is ParticipateMeetingRequest -> meetings.participateMeeting(request) as T
            // not yet supported
            is LinkEmailRequest -> notSupported()
            is ConfirmEmailRequest -> notSupported()
            is GetMeetingRequest -> meetings.getMeeting(request) as T
        }
    }

    private suspend inline fun <T> handleMeetacyExceptions(block: () -> T): T {
        return try {
            block()
        } catch (exception: ResponseException) {
            val response = try {
                Json.decodeFromString<ErrorResponse>(exception.response.body<String>())
            } catch (exception: Throwable) {
                throw MeetacyInternalException(cause = exception)
            }
            throw getException(response.errorCode, response.errorMessage, cause = exception)
        } catch (exception: IOException) {
            throw MeetacyConnectionException(cause = exception)
        } catch (throwable: Throwable) {
            throw MeetacyInternalException(cause = throwable)
        }
    }

    private fun getException(
        code: Int,
        message: String,
        cause: Throwable
    ): Throwable = when (code) {
        MeetacyUnauthorizedException.CODE -> MeetacyUnauthorizedException(message, cause)
        else -> MeetacyInternalException(cause = cause)
    }

    private fun notSupported(): Nothing = TODO("This request is not supported yet!")
}
