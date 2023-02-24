package app.meetacy.sdk.exception

public class UnauthorizedException : RuntimeException(
    message = "Provided token is invalid"
)
