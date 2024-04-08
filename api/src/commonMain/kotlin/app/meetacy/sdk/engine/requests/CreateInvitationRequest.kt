package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.UserId

public data class CreateInvitationRequest(
    val token: Token,
    val usersIds: List<UserId>,
    val meetingId: MeetingId
) : MeetacyRequest<CreateInvitationRequest.Response> {
    public data class Response(val invitations: List<Invitation>)
}
