package app.meetacy.sdk.meetings

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.datetime.DateOrTime
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User

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
    public val date: Date get() = data.date
    public val location: Location get() = data.location
    public val title: String get() = data.title
    public val description: String? get() = data.description
    public val participantsCount: Int get() = data.participantsCount
    public val previewParticipants: List<User> get() = data.previewParticipants
    public val isParticipating: Boolean get() = data.isParticipating
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    public val visibility: Meeting.Visibility get() = data.visibility

    public suspend fun participate(token: Token) {
        api.meetings.participate(token, id)
    }
}
