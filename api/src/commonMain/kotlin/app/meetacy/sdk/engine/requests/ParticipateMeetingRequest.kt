package app.meetacy.sdk.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.meeting.MeetingId

public data class ParticipateMeetingRequest(
    val token: Token,
    val meetingId: MeetingId
) : SimpleMeetacyRequest
