package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId

public data class QuitMeetingRequest(
    val token: Token,
    val meetingId: MeetingId
) : SimpleMeetacyRequest
