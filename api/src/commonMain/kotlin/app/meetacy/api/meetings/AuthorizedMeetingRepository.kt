package app.meetacy.api.meetings

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.files.FileRepository
import app.meetacy.types.location.Location
import app.meetacy.types.datetime.DateOrTime
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.meeting.MeetingId
import app.meetacy.types.user.User

public class AuthorizedMeetingRepository(
    public val data: Meeting,
    private val api: AuthorizedMeetacyApi
) {
    public val base: MeetingRepository get() = MeetingRepository(data, api.base)

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

    public suspend fun participate() {
        api.meetings.participate(data.id)
    }
}
