package app.meetacy.api.meetings

import app.meetacy.api.MeetacyApi
import app.meetacy.api.engine.requests.CreateMeetingRequest
import app.meetacy.api.engine.requests.ParticipateMeetingRequest
import app.meetacy.api.meetings.history.MeetingsHistoryApi
import app.meetacy.api.meetings.map.MeetingsMapApi
import app.meetacy.types.auth.Token
import app.meetacy.types.datetime.Date
import app.meetacy.types.location.Location
import app.meetacy.types.datetime.DateOrTime
import app.meetacy.types.datetime.DateTime
import app.meetacy.types.meeting.MeetingId

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
        description: String? = null
    ): MeetingRepository = create(token, title, DateOrTime.Date(date), location, description)

    public suspend fun create(
        token: Token,
        title: String,
        date: DateTime,
        location: Location,
        description: String? = null
    ): MeetingRepository = create(token, title, DateOrTime.DateTime(date), location, description)

    public suspend fun create(
        token: Token,
        title: String,
        date: DateOrTime,
        location: Location,
        description: String? = null
    ): MeetingRepository {
        val meeting = api.engine.execute(
            request = CreateMeetingRequest(
                token = token,
                title = title,
                date = date,
                location = location,
                description = description
            )
        ).meeting

        return MeetingRepository(meeting, api)
    }

    public suspend fun participate(token: Token, meetingId: MeetingId) {
        api.engine.execute(ParticipateMeetingRequest(token, meetingId))
    }
}
