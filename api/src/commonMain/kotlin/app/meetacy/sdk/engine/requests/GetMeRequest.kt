package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.SelfUser

public data class GetMeRequest(
    override val token: Token
): MeetacyRequest<GetMeRequest.Response>, MeetacyRequestWithToken<GetMeRequest.Response> {
    public data class Response(val me: SelfUser)
}
