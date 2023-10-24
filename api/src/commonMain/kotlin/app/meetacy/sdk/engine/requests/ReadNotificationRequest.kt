package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.notification.NotificationId

public data class ReadNotificationRequest(
    override val token: Token,
    val lastNotificationId: NotificationId
) : SimpleMeetacyRequest, SimpleMeetacyRequestWithToken
