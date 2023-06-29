package app.meetacy.sdk.exception

public fun meetacyApiError(message: String, cause: Throwable? = null): Nothing {
    throw MeetacyInternalException(message, cause)
}
