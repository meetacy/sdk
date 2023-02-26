package app.meetacy.api.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId

public data class GetUserRequest(
    val token: Token,
    val userId: UserId
) : MeetacyRequest<GetUserRequest.Response> {
    public data class Response(
        val user: User
    )
}
