package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.invitation.InvitationId

public class AcceptInvitationRequest(
    public val token: Token,
    public val invitationId: InvitationId
): SimpleMeetacyRequest
