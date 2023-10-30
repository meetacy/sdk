package app.meetacy.sdk.meetings

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.meetings.participants.AuthorizedMeetingParticipantsRepository
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.User

public class AuthorizedMeetingRepository(
    public val data: Meeting,
    private val api: AuthorizedMeetacyApi
) {
    public val base: MeetingRepository get() = MeetingRepository(data, api.base)

    public val id: MeetingId get() = data.id
    public val creator: User get() = data.creator
    public val date: Date get() = data.date
    public val location: Location get() = data.location
    public val title: String? get() = data.title
    public val description: String? get() = data.description
    public val participantsCount: Int get() = data.participantsCount
    public val previewParticipants: List<User> get() = data.previewParticipants
    public val isParticipating: Boolean get() = data.isParticipating
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    public val visibility: Meeting.Visibility get() = data.visibility

    public val participants: AuthorizedMeetingParticipantsRepository =
        AuthorizedMeetingParticipantsRepository(data.id, api)

    public suspend fun edited(
        title: String,
        date: Date,
        location: Location,
        description: String?,
        avatarId: FileId?,
        visibility: Meeting.Visibility
    ): AuthorizedMeetingRepository = edited(
        title = title,
        date = date,
        location = location,
        description = description,
        avatarId = Optional.Present(avatarId),
        visibility = visibility,
    )

    public suspend fun edited(
        title: String?,
        date: Date?,
        location: Location?,
        description: String?,
        avatarId: Optional<FileId?> = Optional.Undefined,
        visibility: Meeting.Visibility?
    ): AuthorizedMeetingRepository {
        return api.meetings.edit(
            meetingId = data.id,
            title = title,
            date = date,
            location = location,
            description = description,
            avatarId = avatarId,
            visibility = visibility
        )
    }

    public suspend fun updated(): AuthorizedMeetingRepository {
        return api.meetings.get(id)
    }

    public suspend fun participate() {
        api.meetings.participate(data.id)
    }
}
