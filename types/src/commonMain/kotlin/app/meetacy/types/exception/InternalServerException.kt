package app.meetacy.types.exception

public class InternalServerException(
    description: String? = null
) : MeetacyException(
    message = description ?: "Internal server exception. Server returned 500"
)

public fun meetacyApiError(error: String): Nothing = throw InternalServerException(error)
