package app.meetacy.sdk.notifications

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.types.user.User

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.notifications.AuthorizedNotificationRepository]
 */
public sealed class NotificationRepository {
    public abstract val data: Notification
    protected abstract val api: NotificationsApi

    public val id: NotificationId get() = data.id
    public val isNew: Boolean get() = data.isNew
    public val date: Date get() = data.date

    public suspend fun read(token: Token) {
        api.read(token, id)
    }

    public data class Subscription(
        override val data: Notification.Subscription,
        override val api: NotificationsApi
    ) : NotificationRepository() {
        public val subscriber: User get() = data.subscriber
    }

    public data class Invitation(
        override val data: Notification.Invitation,
        override val api: NotificationsApi
    ) : NotificationRepository() {
        public val meeting: Meeting get() = data.meeting
        public val inviter: User get() = data.inviter
    }

    public companion object {
        public fun of(data: Notification, base: NotificationsApi): NotificationRepository = when (data) {
            is Notification.Subscription -> Subscription(data, base)
            is Notification.Invitation -> Invitation(data, base)
        }
    }
}
