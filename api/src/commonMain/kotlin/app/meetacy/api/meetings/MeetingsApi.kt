package app.meetacy.api.meetings

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.CreateMeetingRequest
import app.meetacy.api.engine.requests.ParticipateMeetingRequest
import app.meetacy.api.meetings.history.HistoryApi
import app.meetacy.types.auth.Token
import app.meetacy.types.location.Location
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.meeting.MeetingId

public class MeetingsApi(private val engine: MeetacyRequestsEngine) {
    public val history: HistoryApi = HistoryApi(engine)

    public suspend fun create(
        token: Token,
        title: String,
        date: Meeting.DateTimeInfo,
        location: Location,
        description: String? = null
    ): MeetingRepository {
        val meeting = engine.execute(
            request = CreateMeetingRequest(
                token = token,
                title = title,
                date = date,
                location = location,
                description = description
            )
        ).meeting

        return MeetingRepository(meeting, base = this)
    }

    public suspend fun participate(token: Token, meetingId: MeetingId) {
        engine.execute(ParticipateMeetingRequest(token, meetingId))
    }
}
