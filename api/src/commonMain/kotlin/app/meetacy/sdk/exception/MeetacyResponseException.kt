package app.meetacy.sdk.exception

public open class MeetacyResponseException(
    public val code: Int,
    message: String, cause: Throwable?
) : MeetacyException(message, cause)
