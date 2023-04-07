package app.meetacy.sdk.exception

public sealed class MeetacyException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    final override val cause: Throwable? get() = super.cause
    final override val message: String? get() = super.message
}
