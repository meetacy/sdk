package app.meetacy.api.requests

import app.meetacy.sdk.auth.Token
import app.meetacy.sdk.user.User
import app.meetacy.sdk.user.UserId

public data class GetUserRequest(
    val token: Token,
    val userId: UserId
) : MeetacyRequest<GetUserRequest.Response> {
    public data class Response(
        val user: User
    )
}
