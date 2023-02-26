package app.meetacy.api.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.user.SelfUser

public data class GetMeRequest(val token: Token): MeetacyRequest<GetMeRequest.Response> {
    public data class Response(val me: SelfUser)
}
