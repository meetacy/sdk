package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.UserDetails
import app.meetacy.sdk.types.user.UserId

public data class GetUserByIdRequest(
    val token: Token,
    val userId: UserId
) : MeetacyRequest<GetUserByIdRequest.Response> {
    public data class Response(val user: UserDetails)
}
