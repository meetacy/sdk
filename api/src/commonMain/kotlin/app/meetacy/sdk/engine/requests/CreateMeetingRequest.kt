package app.meetacy.sdk.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.location.Location
import app.meetacy.types.datetime.DateOrTime
import app.meetacy.types.meeting.Meeting

public class CreateMeetingRequest(
    public val token: Token,
    public val title: String,
    public val date: DateOrTime,
    public val location: Location,
    public val description: String?,
    public val visibility: Meeting.Visibility
) : MeetacyRequest<CreateMeetingRequest.Response> {
    public data class Response(val meeting: Meeting)
}
