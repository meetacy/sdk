package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

public data class CreateInvitationRequest(
    val token: Token,
    val userId: UserId,
    val meetingId: MeetingId
) : MeetacyRequest<CreateInvitationRequest.Response> {
    public data class Response(val invitation: Invitation)
}
