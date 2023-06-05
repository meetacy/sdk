package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId

public class UpdateInvitationRequest(
    public val token: Token,
    public val id: InvitationId,
    public val expiryDate: DateTime?,
    public val meetingId: MeetingId?
): MeetacyRequest<UpdateInvitationRequest.Response> {
    public data class Response(val result: Invitation)
}
