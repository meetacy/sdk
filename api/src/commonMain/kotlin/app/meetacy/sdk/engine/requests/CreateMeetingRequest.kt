package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting

public class CreateMeetingRequest(
    public val token: Token,
    public val title: String,
    public val date: Date,
    public val location: Location,
    public val description: String?,
    public val visibility: Meeting.Visibility,
    public val fileId: FileId? = null
) : MeetacyRequest<CreateMeetingRequest.Response> {
    public data class Response(val meeting: Meeting)
}
