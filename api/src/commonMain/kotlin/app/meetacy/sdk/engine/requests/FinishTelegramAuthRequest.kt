package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.telegram.SecretTelegramBotKey
import app.meetacy.sdk.types.auth.telegram.TemporalTelegramHash

public data class FinishTelegramAuthRequest(
    val temporalHash: TemporalTelegramHash,
    val secretBotKey: SecretTelegramBotKey,
    val telegramId: Long,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
) : SimpleMeetacyRequest
