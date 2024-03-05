package app.meetacy.sdk.exception

public class MeetacyUserNotFoundException(message: String) : MeetacyResponseException(
    code = CODE,
    message = message,
    cause = null
) {
    public companion object {
        public const val CODE: Int = 8
    }
}
