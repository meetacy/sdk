package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

public data class GetUserRequest(
    override val token: Token,
    val userId: UserId
) : MeetacyRequest<GetUserRequest.Response>, MeetacyRequestWithToken<GetUserRequest.Response> {
    public data class Response(
        val user: User
    )
}
