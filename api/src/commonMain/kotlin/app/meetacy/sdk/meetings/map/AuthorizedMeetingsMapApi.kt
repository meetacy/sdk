package app.meetacy.sdk.meetings.map

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location

public class AuthorizedMeetingsMapApi(
    private val api: AuthorizedMeetacyApi
) {
    public val token: Token get() = api.token
    public val base: MeetingsMapApi get() = api.base.meetings.map

    public suspend fun list(location: Location): List<AuthorizedMeetingRepository> = base
        .list(token, location).map { meeting -> AuthorizedMeetingRepository(meeting.data, api) }
}
