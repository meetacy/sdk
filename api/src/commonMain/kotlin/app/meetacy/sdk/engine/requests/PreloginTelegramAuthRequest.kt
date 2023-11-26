package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.telegram.TempTelegramAuth

public data object PreloginTelegramAuthRequest : MeetacyRequest<PreloginTelegramAuthRequest.Response> {
    public data class Response(
        val tempAuth: TempTelegramAuth
    )
}
