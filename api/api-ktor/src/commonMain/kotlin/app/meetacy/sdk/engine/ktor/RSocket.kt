package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.engine.ktor.exception.getException
import app.meetacy.sdk.engine.ktor.response.ServerResponse
import app.meetacy.sdk.exception.MeetacyConnectionException
import app.meetacy.sdk.exception.MeetacyInternalException
import app.meetacy.sdk.exception.meetacyApiError
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.RSocketError
import io.rsocket.kotlin.ktor.client.rSocket
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json

internal inline fun <T> handleRSocketExceptions(
    json: Json,
    block: () -> T
): T {
    return try {
        block()
    } catch (exception: RSocketError) {
        when (exception) {
            is RSocketError.Custom -> {
                val response = json.decodeFromString<ServerResponse.Error>(
                    string = exception.message ?: meetacyApiError(message = "Message should be present when throwing custom error")
                )
                throw getException(response)
            }
            else -> {
                throw MeetacyConnectionException(cause = exception)
            }
        }
    } catch (exception: IOException) {
        throw MeetacyConnectionException(cause = exception)
    } catch (exception: RuntimeException) {
        throw when (exception) {
            is CancellationException -> exception
            else -> MeetacyInternalException(cause = exception)
        }
    }
}

internal suspend fun HttpClient.meetacyRSocket(
    urlString: String,
    secure: Boolean
): RSocket {
    return rSocket(
        urlString = urlString,
        secure = secure
    ) {
        headers.remove(HttpHeaders.ContentType)
    }
}
