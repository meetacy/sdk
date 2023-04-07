package app.meetacy.sdk.meetings

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.CreateMeetingRequest
import app.meetacy.sdk.engine.requests.ParticipateMeetingRequest
import app.meetacy.sdk.meetings.history.MeetingsHistoryApi
import app.meetacy.sdk.meetings.map.MeetingsMapApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.datetime.DateOrTime
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingsApi]
 * - [MeetingRepository]
 */
public class MeetingsApi(private val api: MeetacyApi) {
    public val history: MeetingsHistoryApi = MeetingsHistoryApi(api)
    public val map: MeetingsMapApi = MeetingsMapApi(api)

    public suspend fun create(
        token: Token,
        title: String,
        date: Date,
        location: Location,
        description: String? = null,
        visibility: Meeting.Visibility = Meeting.Visibility.Private
    ): MeetingRepository {
        val meeting = api.engine.execute(
            request = CreateMeetingRequest(
                token = token,
                title = title,
                date = date,
                location = location,
                description = description,
                visibility = visibility
            )
        ).meeting

        return MeetingRepository(meeting, api)
    }

    public suspend fun participate(token: Token, meetingId: MeetingId) {
        api.engine.execute(ParticipateMeetingRequest(token, meetingId))
    }
}
