package app.meetacy.sdk.exception

public class MeetacyInternalException(
    message: String = "Unknown exception occurred. Please do not try the request with these parameters again",
    cause: Throwable? = null
) : MeetacyException(message, cause)
