package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token

public data class GenerateAuthRequest(
    val nickname: String
) : MeetacyRequest<GenerateAuthRequest.Response> {
    public data class Response(val token: Token)
}
