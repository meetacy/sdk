package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.engine.ktor.exception.getException
import app.meetacy.sdk.engine.ktor.response.ServerResponse
import app.meetacy.sdk.exception.MeetacyConnectionException
import app.meetacy.sdk.exception.MeetacyInternalException
import app.meetacy.sdk.exception.meetacyApiError
import io.ktor.utils.io.errors.*
import io.rsocket.kotlin.RSocketError
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
