package app.meetacy.sdk.auth.telegram

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.AwaitTelegramAuthRequest
import app.meetacy.sdk.types.auth.Token

public class AuthTelegramApi(private val api: MeetacyApi) {
    public suspend fun await(temporalToken: Token): Token {
        return api.engine.execute(AwaitTelegramAuthRequest(temporalToken)).token
    }
}
