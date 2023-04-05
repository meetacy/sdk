package app.meetacy.sdk.meetings.map

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingsMapRequest
import app.meetacy.sdk.meetings.MeetingRepository
import app.meetacy.types.auth.Token

public class MeetingsMapApi(
    private val api: MeetacyApi
) {
    public suspend fun list(token: Token): List<MeetingRepository> =
        api.engine
            .execute(ListMeetingsMapRequest(token))
            .meetings.map { meeting -> MeetingRepository(meeting, api) }
}
