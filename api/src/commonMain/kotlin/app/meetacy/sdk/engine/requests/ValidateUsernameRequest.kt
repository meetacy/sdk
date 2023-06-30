package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.user.Username

public data class ValidateUsernameRequest(
    val username: Username
) : MeetacyRequest<ValidateUsernameRequest.Response> {
    public data class Response(val username: Username)
}
