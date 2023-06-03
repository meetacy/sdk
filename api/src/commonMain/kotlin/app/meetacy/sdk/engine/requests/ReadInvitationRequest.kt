package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.user.UserId

public class ReadInvitationRequest(
    public val token: Token,
    public val from: List<UserId>? = null,
    public val ids: List<InvitationId>? = null
): MeetacyRequest<ReadInvitationRequest.Response> {
    public data class Response(val result: List<Invitation>)
}
