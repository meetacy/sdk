package app.meetacy.sdk.types.notification

import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.User

public sealed interface Notification {
    public val id: NotificationId
    public val isNew: Boolean
    public val date: Date

    public data class Subscription(
        override val id: NotificationId,
        override val isNew: Boolean,
        override val date: Date,
        public val subscriber: RegularUser,
    ) : Notification

    public data class Invitation(
        override val id: NotificationId,
        override val isNew: Boolean,
        override val date: Date,
        public val meeting: Meeting,
        public val inviter: RegularUser
    ) : Notification
}
