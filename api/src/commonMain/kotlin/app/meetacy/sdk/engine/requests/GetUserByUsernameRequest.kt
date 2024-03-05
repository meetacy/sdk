package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.UserDetails
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username

public data class GetUserByUsernameRequest(
    val token: Token,
    val username: Username
) : MeetacyRequest<GetUserByUsernameRequest.Response> {
    public data class Response(val user: UserDetails)
}
