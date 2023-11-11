@file:OptIn(UnstableApi::class)

package app.meetacy.sdk.auth.telegram

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.annotation.UnstableApi
import app.meetacy.sdk.types.auth.telegram.TempTelegramAuth
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.url.Url

public class TempAuthRepository(
    public val data: TempTelegramAuth,
    private val api: MeetacyApi
) {
    public val token: Token get() = data.token
    public val botLink: Url get() = data.botLink

    public suspend fun await(): AuthorizedMeetacyApi {
        return api.auth.telegram.await(token)
    }
}
