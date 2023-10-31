package app.meetacy.sdk.types.meeting

import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.user.User

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.meetings.MeetingRepository]
 * - [app.meetacy.sdk.meetings.AuthorizedMeetingRepository]
 */
public data class Meeting(
    val id: MeetingId,
    val creator: User,
    val date: Date,
    val location: Location,
    val title: String,
    val description: String?,
    val participantsCount: Int,
    val previewParticipants: List<User>,
    val isParticipating: Boolean,
    val avatarId: FileId?,
    val visibility: Visibility
) {
    public enum class Visibility {
        Public, Private
    }
}
