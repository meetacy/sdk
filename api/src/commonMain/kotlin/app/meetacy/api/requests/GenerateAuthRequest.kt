package app.meetacy.api.requests

import app.meetacy.sdk.auth.Token

public data class GenerateAuthRequest(
    val nickname: String
) : MeetacyRequest<GenerateAuthRequest.Response> {
    public data class Response(val token: Token)
}
