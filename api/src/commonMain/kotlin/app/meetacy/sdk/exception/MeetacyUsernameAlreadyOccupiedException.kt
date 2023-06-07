package app.meetacy.sdk.exception

public class MeetacyUsernameAlreadyOccupiedException(
    message: String,
    cause: Throwable? = null
) : MeetacyResponseException(
    code = CODE,
    message = message,
    cause = cause
) {
    public companion object {
        public const val CODE: Int = 6
    }
}
