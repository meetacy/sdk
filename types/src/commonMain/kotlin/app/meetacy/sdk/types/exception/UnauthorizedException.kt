package app.meetacy.sdk.types.exception

public class UnauthorizedException : MeetacyException(
    message = "Provided token is invalid or expired"
)