package app.meetacy.sdk.meetings

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.types.auth.Token
import app.meetacy.types.location.Location
import app.meetacy.types.datetime.DateOrTime
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.meeting.MeetingId
import app.meetacy.types.user.User

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingRepository]
 */
public class MeetingRepository(
    public val data: Meeting,
    private val api: MeetacyApi
) {
    public val id: MeetingId get() = data.id
    public val creator: User get() = data.creator
    public val date: DateOrTime get() = data.date
    public val location: Location get() = data.location
    public val title: String get() = data.title
    public val description: String? get() = data.description
    public val participantsCount: Int get() = data.participantsCount
    public val previewParticipants: List<User> get() = data.previewParticipants
    public val isParticipating: Boolean get() = data.isParticipating
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)

    public suspend fun participate(token: Token) {
        api.meetings.participate(token, id)
    }
}
