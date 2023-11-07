package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.user.Username

public data class UsernameAvailableRequest(
    val username: Username
) : MeetacyRequest<UsernameAvailableRequest.Response> {
    public data class Response(val username: Username)
}
