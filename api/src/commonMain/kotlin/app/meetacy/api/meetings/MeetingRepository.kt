package app.meetacy.api.meetings

import app.meetacy.types.auth.Token
import app.meetacy.types.datetime.Date
import app.meetacy.types.file.FileId
import app.meetacy.types.location.Location
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.meeting.MeetingId
import app.meetacy.types.user.User

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingRepository]
 */
public class MeetingRepository(
    public val data: Meeting,
    public val base: MeetingsApi
) {
    public val id: MeetingId get() = data.id
    public val creator: User get() = data.creator
    public val date: Meeting.DateTimeInfo get() = data.date
    public val location: Location get() = data.location
    public val title: String get() = data.title
    public val description: String? get() = data.description
    public val participantsCount: Int get() = data.participantsCount
    public val isParticipating: Boolean get() = data.isParticipating
    public val avatarIdentity: FileId? get() = data.avatarIdentity

    public suspend fun participate(token: Token) {
        base.participate(token, data.id)
    }
}
