package app.meetacy.api.meetings

import app.meetacy.api.meetings.history.AuthorizedHistoryApi
import app.meetacy.types.auth.Token
import app.meetacy.types.location.Location
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.meeting.MeetingId

public class AuthorizedMeetingsApi(
    public val token: Token,
    public val base: MeetingsApi
) {
    public val history: AuthorizedHistoryApi = AuthorizedHistoryApi(token, base.history)

    public suspend fun create(
        title: String,
        date: Meeting.DateTimeInfo,
        location: Location,
        description: String? = null
    ): AuthorizedMeetingRepository {
        val repository = base.create(
            token = token,
            title = title,
            date = date,
            location = location,
            description = description
        )

        return AuthorizedMeetingRepository(repository.data, base = this)
    }

    public suspend fun participate(meetingId: MeetingId) {
        base.participate(token, meetingId)
    }
}
