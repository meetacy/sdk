package app.meetacy.sdk.types.serializable.meeting

import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.serializable.datetime.DateSerializable
import app.meetacy.sdk.types.serializable.datetime.serializable
import app.meetacy.sdk.types.serializable.datetime.type
import app.meetacy.sdk.types.serializable.file.FileIdSerializable
import app.meetacy.sdk.types.serializable.file.serializable
import app.meetacy.sdk.types.serializable.file.type
import app.meetacy.sdk.types.serializable.location.LocationSerializable
import app.meetacy.sdk.types.serializable.location.serializable
import app.meetacy.sdk.types.serializable.location.type
import app.meetacy.sdk.types.serializable.user.UserSerializable
import app.meetacy.sdk.types.serializable.user.serializable
import app.meetacy.sdk.types.serializable.user.type
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MeetingSerializable(
    val id: MeetingIdSerializable,
    val creator: UserSerializable,
    val date: DateSerializable,
    val location: LocationSerializable,
    val title: String,
    val description: String? = null,
    val participantsCount: Int,
    val previewParticipants: List<UserSerializable>,
    val isParticipating: Boolean,
    val avatarId: FileIdSerializable? = null,
    val visibility: Visibility
) {
    @Serializable
    public enum class Visibility {
        @SerialName("public")
        Public,
        @SerialName("private")
        Private
    }
}

public fun MeetingSerializable.type(): Meeting = Meeting(
    id = id.type(),
    creator = creator.type(),
    date = date.type(),
    location = location.type(),
    title = title,
    description = description,
    participantsCount = participantsCount,
    previewParticipants = previewParticipants.map { it.type() },
    isParticipating = isParticipating,
    avatarId = avatarId?.type(),
    visibility = visibility.type()
)

public fun MeetingSerializable.Visibility.type(): Meeting.Visibility = when (this) {
    MeetingSerializable.Visibility.Public -> Meeting.Visibility.Public
    MeetingSerializable.Visibility.Private -> Meeting.Visibility.Private
}

public fun Meeting.Visibility.serializable(): MeetingSerializable.Visibility = when (this) {
    Meeting.Visibility.Public -> MeetingSerializable.Visibility.Public
    Meeting.Visibility.Private -> MeetingSerializable.Visibility.Private
}

public fun Meeting.serializable(): MeetingSerializable = MeetingSerializable(
    id = id.serializable(),
    creator = creator.serializable(),
    date = date.serializable(),
    location = location.serializable(),
    title = title,
    description = description,
    participantsCount = participantsCount,
    previewParticipants = previewParticipants.map { it.serializable() },
    isParticipating = isParticipating,
    avatarId = avatarId?.serializable(),
    visibility = visibility.serializable()
)

