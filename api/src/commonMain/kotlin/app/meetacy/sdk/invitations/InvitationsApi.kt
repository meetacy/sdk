package app.meetacy.sdk.invitations

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.AcceptInvitationRequest
import app.meetacy.sdk.engine.requests.CancelInvitationRequest
import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.engine.requests.DenyInvitationRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.UserId

public class InvitationsApi(private val api: MeetacyApi) {
    public suspend fun create(
        token: Token,
        usersIds: List<UserId>,
        meetingId: MeetingId
    ): List<InvitationsRepository> {
        val invitations = api.engine.execute(
            request = CreateInvitationRequest(
                token,
                usersIds,
                meetingId
            )
        ).invitations

        return invitations.map { InvitationsRepository(it, api) }
    }

    public suspend fun accept(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(request = AcceptInvitationRequest(token, invitationId))
    }

    public suspend fun deny(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(request = DenyInvitationRequest(token, invitationId))
    }

    public suspend fun cancel(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(request = CancelInvitationRequest(token, invitationId))
    }
}
