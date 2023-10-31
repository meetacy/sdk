package app.meetacy.sdk.meetings

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.history.AuthorizedMeetingsHistoryApi
import app.meetacy.sdk.meetings.map.AuthorizedMeetingsMapApi
import app.meetacy.sdk.meetings.participants.AuthorizedMeetingParticipantsApi
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.optional.Optional

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingRepository]
 */
public class AuthorizedMeetingsApi(
    private val api: AuthorizedMeetacyApi
) {
    public val base: MeetingsApi get() = api.base.meetings

    public val history: AuthorizedMeetingsHistoryApi = AuthorizedMeetingsHistoryApi(api)
    public val map: AuthorizedMeetingsMapApi = AuthorizedMeetingsMapApi(api)
    public val participants: AuthorizedMeetingParticipantsApi = AuthorizedMeetingParticipantsApi(api)

    public suspend fun create(
        title: String?,
        date: Date,
        location: Location,
        description: String? = null,
        visibility: Meeting.Visibility = Meeting.Visibility.Private,
        fileId: FileId? = null
    ): AuthorizedMeetingRepository {
        val repository = base.create(
            token = api.token,
            title = title,
            date = date,
            location = location,
            description = description,
            visibility = visibility,
            fileId = fileId
        )

        return AuthorizedMeetingRepository(repository.data, api)
    }

    public suspend fun edit(
        meetingId: MeetingId,
        title: String,
        date: Date,
        location: Location,
        description: String?,
        avatarId: FileId?,
        visibility: Meeting.Visibility
    ): AuthorizedMeetingRepository = edit(
        meetingId = meetingId,
        title = Optional.Present(title),
        date = Optional.Present(date),
        location = Optional.Present(location),
        description = Optional.Present(description),
        avatarId = Optional.Present(avatarId),
        visibility = Optional.Present(visibility),
    )

    public suspend fun edit(
        meetingId: MeetingId,
        title: Optional<String> = Optional.Undefined,
        date: Optional<Date> = Optional.Undefined,
        location: Optional<Location> = Optional.Undefined,
        description: Optional<String?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined,
        visibility: Optional<Meeting.Visibility> = Optional.Undefined
    ): AuthorizedMeetingRepository {
        val meeting = api.base.meetings.edit(
            token = api.token,
            meetingId = meetingId,
            title = title,
            date = date,
            location = location,
            description = description,
            avatarId = avatarId,
            visibility = visibility
        )

        return AuthorizedMeetingRepository(
            data = meeting.data,
            api = api
        )
    }

    public suspend fun participate(meetingId: MeetingId) {
        base.participate(api.token, meetingId)
    }

    public suspend fun get(meetingId: MeetingId): AuthorizedMeetingRepository {
        val repository = base.get(api.token, meetingId)

        return AuthorizedMeetingRepository(repository.data, api)
    }
}
