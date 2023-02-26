package app.meetacy.types.exception

public sealed class MeetacyException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
