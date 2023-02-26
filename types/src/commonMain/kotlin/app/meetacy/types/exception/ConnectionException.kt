package app.meetacy.types.exception

public class ConnectionException(cause: Throwable) : MeetacyException(
    message = "Error while connecting to server",
    cause = cause
)
