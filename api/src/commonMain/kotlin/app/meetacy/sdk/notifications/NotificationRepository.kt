package app.meetacy.sdk.notifications

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.meetings.MeetingRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.users.RegularUserRepository
import app.meetacy.sdk.users.UserRepository

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.notifications.AuthorizedNotificationRepository]
 */
public sealed class NotificationRepository {
    public abstract val data: Notification
    protected abstract val api: MeetacyApi

    public val id: NotificationId get() = data.id
    public val isNew: Boolean get() = data.isNew
    public val date: DateTime get() = data.date

    public suspend fun read(token: Token) {
        api.notifications.read(token, id)
    }

    public data class Subscription(
        override val data: Notification.Subscription,
        override val api: MeetacyApi
    ) : NotificationRepository() {
        public val subscriber: RegularUserRepository
            get() = RegularUserRepository(data.subscriber, api)
    }

    public data class Invitation(
        override val data: Notification.Invitation,
        override val api: MeetacyApi
    ) : NotificationRepository() {
        public val meeting: MeetingRepository
            get() = MeetingRepository(data.meeting, api)
        public val inviter: UserRepository
            get() = UserRepository.of(data.inviter, api)
    }

    public companion object {
        public fun of(data: Notification, api: MeetacyApi): NotificationRepository = when (data) {
            is Notification.Subscription -> Subscription(data, api)
            is Notification.Invitation -> Invitation(data, api)
        }
    }
}
