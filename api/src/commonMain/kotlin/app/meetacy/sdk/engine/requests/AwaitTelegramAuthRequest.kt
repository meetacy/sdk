package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token

public class AwaitTelegramAuthRequest(
    public val temporalToken: Token
) : MeetacyRequest<AwaitTelegramAuthRequest.Response> {
    public data class Response(val token: Token)
}
