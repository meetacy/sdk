package app.meetacy.sdk.types.serializable.notification

import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.serializable.datetime.DateTimeSerializable
import app.meetacy.sdk.types.serializable.datetime.type
import app.meetacy.sdk.types.serializable.meeting.MeetingSerializable
import app.meetacy.sdk.types.serializable.meeting.type
import app.meetacy.sdk.types.serializable.user.UserSerializable
import app.meetacy.sdk.types.serializable.user.type
import app.meetacy.sdk.types.user.RegularUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class NotificationSerializable {
    public abstract val id: NotificationIdSerializable
    public abstract val isNew: Boolean
    public abstract val date: DateTimeSerializable

    @SerialName("subscription")
    @Serializable
    public class Subscription(
        override val isNew: Boolean,
        override val id: NotificationIdSerializable,
        override val date: DateTimeSerializable,
        public val subscriber: UserSerializable,
    ) : NotificationSerializable()

    @SerialName("meeting_invitation")
    @Serializable
    public class Invitation(
        override val id: NotificationIdSerializable,
        override val isNew: Boolean,
        override val date: DateTimeSerializable,
        public val meeting: MeetingSerializable,
        public val inviter: UserSerializable,
    ) : NotificationSerializable()
}

public fun NotificationSerializable.type(): Notification =
    when (this) {
        is NotificationSerializable.Invitation -> Notification.Invitation(
            id = id.type(),
            isNew = isNew,
            meeting = meeting.type(),
            date = date.type(),
            inviter = inviter.type() as RegularUser
        )
        is NotificationSerializable.Subscription -> Notification.Subscription(
            id = id.type(),
            isNew = isNew,
            subscriber = subscriber.type() as RegularUser,
            date = date.type()
        )
    }
