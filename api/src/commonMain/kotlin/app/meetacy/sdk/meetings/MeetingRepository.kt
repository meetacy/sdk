package app.meetacy.sdk.meetings

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.meetings.participants.MeetingParticipantsRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.datetime.DateOrTime
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.optional.Optional
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

    public val participants: MeetingParticipantsRepository = MeetingParticipantsRepository(data.id, api)

    public suspend fun edited(
        token: Token,
        title: String,
        date: Date,
        location: Location,
        description: String?,
        avatarId: FileId?,
        visibility: Meeting.Visibility
    ): MeetingRepository = edited(
        token = token,
        title = Optional.Present(title),
        date = Optional.Present(date),
        location = Optional.Present(location),
        description = Optional.Present(description),
        avatarId = Optional.Present(avatarId),
        visibility = Optional.Present(visibility),
    )

    public suspend fun edited(
        token: Token,
        title: Optional<String> = Optional.Undefined,
        date: Optional<Date> = Optional.Undefined,
        location: Optional<Location> = Optional.Undefined,
        description: Optional<String?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined,
        visibility: Optional<Meeting.Visibility> = Optional.Undefined
    ): MeetingRepository {
        return api.meetings.edit(
            token = token,
            meetingId = data.id,
            title = title,
            date = date,
            location = location,
            description = description,
            avatarId = avatarId,
            visibility = visibility
        )
    }

    public suspend fun participate(token: Token) {
        api.meetings.participate(token, id)
    }

    public suspend fun updated(
        token: Token
    ): MeetingRepository {
        return api.meetings.get(token, id)
    }
}
