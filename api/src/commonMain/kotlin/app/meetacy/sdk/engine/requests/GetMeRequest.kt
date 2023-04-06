package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.SelfUser

public data class GetMeRequest(val token: Token): MeetacyRequest<GetMeRequest.Response> {
    public data class Response(val me: SelfUser)
}
