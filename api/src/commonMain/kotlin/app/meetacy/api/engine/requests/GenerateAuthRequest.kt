package app.meetacy.api.engine.requests

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.types.auth.Token

public data class GenerateAuthRequest(
    val nickname: String
) : MeetacyRequest<GenerateAuthRequest.Response> {
    public data class Response(val token: Token)
}
