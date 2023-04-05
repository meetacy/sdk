package app.meetacy.api.meetings

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.meetings.history.AuthorizedMeetingsHistoryApi
import app.meetacy.api.meetings.map.AuthorizedMeetingsMapApi
import app.meetacy.types.datetime.Date
import app.meetacy.types.location.Location
import app.meetacy.types.datetime.DateOrTime
import app.meetacy.types.datetime.DateTime
import app.meetacy.types.meeting.MeetingId

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

    public suspend fun create(
        title: String,
        date: Date,
        location: Location,
        description: String? = null
    ): AuthorizedMeetingRepository = create(title, DateOrTime.Date(date), location, description)

    public suspend fun create(
        title: String,
        date: DateTime,
        location: Location,
        description: String? = null
    ): AuthorizedMeetingRepository = create(title, DateOrTime.DateTime(date), location, description)

    public suspend fun create(
        title: String,
        date: DateOrTime,
        location: Location,
        description: String? = null
    ): AuthorizedMeetingRepository {
        val repository = base.create(
            token = api.token,
            title = title,
            date = date,
            location = location,
            description = description
        )

        return AuthorizedMeetingRepository(repository.data, api)
    }

    public suspend fun participate(meetingId: MeetingId) {
        base.participate(api.token, meetingId)
    }
}
