package app.meetacy.types.meeting

import app.meetacy.types.datetime.DateOrTime
import app.meetacy.types.file.FileId
import app.meetacy.types.location.Location
import app.meetacy.types.user.User

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.api.meetings.MeetingRepository]
 * - [app.meetacy.api.meetings.AuthorizedMeetingRepository]
 */
public data class Meeting(
    val id: MeetingId,
    val creator: User,
    val date: DateOrTime,
    val location: Location,
    val title: String,
    val description: String?,
    val participantsCount: Int,
    val previewParticipants: List<User>,
    val isParticipating: Boolean,
    val avatarId: FileId?
) {
}
