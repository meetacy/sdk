package app.meetacy.sdk.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.location.Location
import app.meetacy.types.meeting.Meeting

public data class ListMeetingsMapRequest(
    val token: Token,
    val location: Location
) : MeetacyRequest<ListMeetingsMapRequest.Response> {
    public data class Response(val meetings: List<Meeting>)
}
