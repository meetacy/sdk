package app.meetacy.api.requests

import app.meetacy.sdk.auth.Token
import app.meetacy.sdk.user.SelfUser

public data class GetMeRequest(val token: Token): MeetacyRequest<GetMeRequest.Response> {
    public data class Response(val me: SelfUser)
}
