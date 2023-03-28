package app.meetacy.api.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.datetime.Date
import app.meetacy.types.location.Location
import app.meetacy.types.meeting.Meeting

public class CreateMeetingRequest(
    public val token: Token,
    public val title: String,
    public val date: Meeting.DateTimeInfo,
    public val location: Location,
    public val description: String?
) : MeetacyRequest<CreateMeetingRequest.Response> {
    public data class Response(val meeting: Meeting)
}
