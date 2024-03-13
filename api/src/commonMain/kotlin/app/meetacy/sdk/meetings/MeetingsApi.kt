package app.meetacy.sdk.meetings

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.meetings.history.MeetingsHistoryApi
import app.meetacy.sdk.meetings.map.MeetingsMapApi
import app.meetacy.sdk.meetings.participants.MeetingParticipantsApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.optional.Optional

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingsApi]
 * - [MeetingRepository]
 */
public class MeetingsApi(private val api: MeetacyApi) {
    public val history: MeetingsHistoryApi = MeetingsHistoryApi(api)
    public val map: MeetingsMapApi = MeetingsMapApi(api)
    public val participants: MeetingParticipantsApi = MeetingParticipantsApi(api)

    public suspend fun create(
        token: Token,
        title: String?,
        date: Date,
        location: Location,
        description: String? = null,
        visibility: Meeting.Visibility = Meeting.Visibility.Private,
        fileId: FileId? = null
    ): MeetingRepository {
        val meeting = api.engine.execute(
            request = CreateMeetingRequest(
                token = token,
                title = title,
                date = date,
                location = location,
                description = description,
                visibility = visibility,
                fileId = fileId
            )
        ).meeting

        return MeetingRepository(meeting, api)
    }

    public suspend fun edit(
        token: Token,
        meetingId: MeetingId,
        title: String,
        date: Date,
        location: Location,
        description: String?,
        avatarId: FileId?,
        visibility: Meeting.Visibility
    ): MeetingRepository = edit(
        token = token,
        meetingId = meetingId,
        title = Optional.Present(title),
        date = Optional.Present(date),
        location = Optional.Present(location),
        description = Optional.Present(description),
        avatarId = Optional.Present(avatarId),
        visibility = Optional.Present(visibility),
    )

    public suspend fun edit(
        token: Token,
        meetingId: MeetingId,
        title: Optional<String> = Optional.Undefined,
        date: Optional<Date> = Optional.Undefined,
        location: Optional<Location> = Optional.Undefined,
        description: Optional<String?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined,
        visibility: Optional<Meeting.Visibility> = Optional.Undefined
    ): MeetingRepository {
        val meeting = api.engine.execute(
            EditMeetingRequest(
                token = token,
                meetingId = meetingId,
                title = title,
                date = date,
                location = location,
                description = description,
                avatarId = avatarId,
                visibility = visibility
            )
        ).meeting

        return MeetingRepository(
            data = meeting,
            api = api
        )
    }

    public suspend fun participate(token: Token, meetingId: MeetingId) {
        api.engine.execute(ParticipateMeetingRequest(token, meetingId))
    }

    public suspend fun leave(token: Token, meetingId: MeetingId) {
        api.engine.execute(LeaveMeetingRequest(token, meetingId))
    }

    public suspend fun get(token: Token, meetingId: MeetingId): MeetingRepository {
        val meeting = api.engine.execute(GetMeetingRequest(token, meetingId)).meeting

        return MeetingRepository(meeting, api)
    }
}
