package app.meetacy.api.meetings.map

import app.meetacy.api.MeetacyApi
import app.meetacy.api.engine.requests.ListMeetingsMapRequest
import app.meetacy.api.meetings.MeetingRepository
import app.meetacy.types.auth.Token

public class MeetingsMapApi(
    private val api: MeetacyApi
) {
    public suspend fun list(token: Token): List<MeetingRepository> =
        api.engine
            .execute(ListMeetingsMapRequest(token))
            .meetings.map { meeting -> MeetingRepository(meeting, api) }
}
