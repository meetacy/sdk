@file:OptIn(UnstableApi::class)

package app.meetacy.sdk.types.auth.telegram

import app.meetacy.sdk.types.annotation.UnstableApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.url.Url


/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.auth.telegram.TempAuthRepository]
 */
public data class TempTelegramAuth(
    val token: Token,
    val botLink: Url
)
