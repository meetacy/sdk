package app.meetacy.sdk.meetings

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.history.AuthorizedMeetingsHistoryApi
import app.meetacy.sdk.meetings.map.AuthorizedMeetingsMapApi
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.datetime.DateOrTime
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId

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
        description: String? = null,
        visibility: Meeting.Visibility = Meeting.Visibility.Private
    ): AuthorizedMeetingRepository {
        val repository = base.create(
            token = api.token,
            title = title,
            date = date,
            location = location,
            description = description,
            visibility = visibility
        )

        return AuthorizedMeetingRepository(repository.data, api)
    }

    public suspend fun participate(meetingId: MeetingId) {
        base.participate(api.token, meetingId)
    }
}
