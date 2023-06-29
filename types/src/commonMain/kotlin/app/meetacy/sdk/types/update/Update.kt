package app.meetacy.sdk.types.update

import app.meetacy.sdk.types.notification.Notification as NotificationType

public sealed interface Update {
    public val id: UpdateId

    public data class Notification(
        override val id: UpdateId,
        val notification: NotificationType
    ) : Update
}
