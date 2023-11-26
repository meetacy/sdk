package app.meetacy.sdk.auth.telegram

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.AwaitTelegramAuthRequest
import app.meetacy.sdk.engine.requests.FinishTelegramAuthRequest
import app.meetacy.sdk.engine.requests.PreloginTelegramAuthRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.auth.telegram.SecretTelegramBotKey
import app.meetacy.sdk.types.auth.telegram.TemporalTelegramHash

public class AuthTelegramApi(private val api: MeetacyApi) {
    public suspend fun await(temporalToken: Token): AuthorizedMeetacyApi {
        val token = api.engine.execute(AwaitTelegramAuthRequest(temporalToken)).permanentToken
        return api.authorized(token)
    }

    public suspend fun finish(
        temporalHash: TemporalTelegramHash,
        secretBotKey: SecretTelegramBotKey,
        telegramId: Long,
        username: String?,
        firstName: String?,
        lastName: String?,
    ) {
        val request = FinishTelegramAuthRequest(
            temporalHash = temporalHash,
            secretBotKey = secretBotKey,
            telegramId = telegramId,
            username = username,
            firstName = firstName,
            lastName = lastName,
        )
        api.engine.execute(request)
    }

    public suspend fun prelogin(): TempAuthRepository {
        val tempAuth = api.engine.execute(PreloginTelegramAuthRequest).tempAuth
        return TempAuthRepository(tempAuth, api)
    }
}
