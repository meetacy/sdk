package app.meetacy.sdk.types.exception

public class InternalServerException(
    description: String? = null
) : MeetacyException(
    message = description ?: "Internal server exception. Server returned 500"
)

internal fun meetacyApiError(error: String): Nothing = throw InternalServerException(error)
