package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId

public data class GetMeetingRequest(
    val token: Token,
    val meetingId: MeetingId
) : MeetacyRequest<GetMeetingRequest.Response> {
    public data class Response(val meeting: Meeting)
}
