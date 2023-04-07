package app.meetacy.sdk.exception

public class MeetacyUnauthorizedException(
    message: String = "Your token was expired or invalid, please generate a new one",
    cause: Throwable? = null
) : MeetacyResponseException(
    code = CODE,
    message = message,
    cause = cause
) {
    public companion object {
        public const val CODE: Int = 1
    }
}
