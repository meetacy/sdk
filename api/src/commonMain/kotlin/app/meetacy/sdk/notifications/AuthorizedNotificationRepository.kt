package app.meetacy.sdk.notifications

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.types.user.User

public sealed class AuthorizedNotificationRepository {
    protected abstract val api: AuthorizedNotificationsApi
    public abstract val data: Notification
    public abstract val base: NotificationRepository

    public val token: Token get() = api.token

    public val id: NotificationId get() = data.id
    public val isNew: Boolean get() = data.isNew
    public val date: Date get() = data.date

    public suspend fun read() {
        api.read(id)
    }

    public data class Subscription(
        override val data: Notification.Subscription,
        override val api: AuthorizedNotificationsApi
    ) : AuthorizedNotificationRepository() {
        override val base: NotificationRepository.Subscription
            get() = NotificationRepository.Subscription(data, api.base)

        public val subscriber: User get() = data.subscriber
    }

    public data class Invitation(
        override val data: Notification.Invitation,
        override val api: AuthorizedNotificationsApi
    ) : AuthorizedNotificationRepository() {
        override val base: NotificationRepository.Invitation
            get() = NotificationRepository.Invitation(data, api.base)

        public val meeting: Meeting get() = data.meeting
        public val inviter: User get() = data.inviter
    }

    public companion object {
        public fun of(
            data: Notification,
            api: AuthorizedNotificationsApi
        ): AuthorizedNotificationRepository = when (data) {
            is Notification.Subscription -> Subscription(data, api)
            is Notification.Invitation -> Invitation(data, api)
        }
    }
}
