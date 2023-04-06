package app.meetacy.sdk.meetings.map

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingsMapRequest
import app.meetacy.sdk.meetings.MeetingRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingsMapApi]
 */
public class MeetingsMapApi(
    private val api: MeetacyApi
) {
    public suspend fun list(token: Token, location: Location): List<MeetingRepository> =
        api.engine
            .execute(ListMeetingsMapRequest(token, location))
            .meetings.map { meeting -> MeetingRepository(meeting, api) }
}
