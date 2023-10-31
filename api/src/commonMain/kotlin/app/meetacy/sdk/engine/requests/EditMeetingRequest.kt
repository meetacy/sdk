package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.optional.Optional

public data class EditMeetingRequest(
    public val token: Token,
    public val meetingId: MeetingId,
    public val title: Optional<String>,
    public val description: Optional<String>,
    public val location: Optional<Location>,
    public val date: Optional<Date>,
    public val avatarId: Optional<FileId?>,
    public val visibility: Optional<Meeting.Visibility>
) : MeetacyRequest<EditMeetingRequest.Response> {
    public data class Response(val meeting: Meeting)
}
