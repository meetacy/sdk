package app.meetacy.sdk.exception

public class MeetacyConnectionException(
    message: String = "Cannot perform request because of poor connection",
    cause: Throwable? = null
) : MeetacyException(message, cause)
