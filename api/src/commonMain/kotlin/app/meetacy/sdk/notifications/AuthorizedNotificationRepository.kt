package app.meetacy.sdk.notifications

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.users.AuthorizedRegularUserRepository

public sealed class AuthorizedNotificationRepository {
    protected abstract val api: AuthorizedMeetacyApi
    public abstract val data: Notification
    public abstract val base: NotificationRepository

    public val token: Token get() = api.token

    public val id: NotificationId get() = data.id
    public val isNew: Boolean get() = data.isNew
    public val date: DateTime get() = data.date

    public suspend fun read() {
        api.notifications.read(id)
    }

    public data class Subscription(
        override val data: Notification.Subscription,
        override val api: AuthorizedMeetacyApi
    ) : AuthorizedNotificationRepository() {
        override val base: NotificationRepository.Subscription
            get() = NotificationRepository.Subscription(data, api.base)

        public val subscriber: AuthorizedRegularUserRepository
            get() = AuthorizedRegularUserRepository(data.subscriber, api)
    }

    public data class Invitation(
        override val data: Notification.Invitation,
        override val api: AuthorizedMeetacyApi
    ) : AuthorizedNotificationRepository() {
        override val base: NotificationRepository.Invitation
            get() = NotificationRepository.Invitation(data, api.base)

        public val meeting: AuthorizedMeetingRepository
            get() = AuthorizedMeetingRepository(data.meeting, api)
        public val inviter: AuthorizedRegularUserRepository
            get() = AuthorizedRegularUserRepository(data.inviter, api)
    }

    public companion object {
        public fun of(
            data: Notification,
            api: AuthorizedMeetacyApi
        ): AuthorizedNotificationRepository = when (data) {
            is Notification.Subscription -> Subscription(data, api)
            is Notification.Invitation -> Invitation(data, api)
        }
    }
}
