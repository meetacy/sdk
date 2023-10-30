package app.meetacy.sdk.types.notification

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.user.RegularUser

public sealed interface Notification {
    public val id: NotificationId
    public val isNew: Boolean
    public val date: DateTime

    public data class Subscription(
        override val id: NotificationId,
        override val isNew: Boolean,
        override val date: DateTime,
        public val subscriber: RegularUser,
    ) : Notification

    public data class Invitation(
        override val id: NotificationId,
        override val isNew: Boolean,
        override val date: DateTime,
        public val meeting: Meeting,
        public val inviter: RegularUser
    ) : Notification
}
