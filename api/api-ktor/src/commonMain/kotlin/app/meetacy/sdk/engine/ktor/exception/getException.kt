package app.meetacy.sdk.engine.ktor.exception

import app.meetacy.sdk.engine.ktor.response.ServerResponse
import app.meetacy.sdk.exception.MeetacyInternalException
import app.meetacy.sdk.exception.MeetacyUnauthorizedException
import app.meetacy.sdk.exception.MeetacyUsernameAlreadyOccupiedException

internal fun getException(
    error: ServerResponse.Error
): Throwable {
    return when (error.errorCode) {
        MeetacyUnauthorizedException.CODE -> MeetacyUnauthorizedException(error.errorMessage)
        MeetacyUsernameAlreadyOccupiedException.CODE -> MeetacyUsernameAlreadyOccupiedException(error.errorMessage)
        else -> MeetacyInternalException(error.errorMessage)
    }
}
