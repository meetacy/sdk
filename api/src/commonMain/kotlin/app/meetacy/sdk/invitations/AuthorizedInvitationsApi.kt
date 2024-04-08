package app.meetacy.sdk.invitations

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.UserId

public class AuthorizedInvitationsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: InvitationsApi = api.base.invitations

    public suspend fun create(
        usersIds: List<UserId>,
        meetingId: MeetingId
    ): List<AuthorizedInvitationRepository> {
        val invitations = base.create(token, usersIds, meetingId)

        return invitations.map { AuthorizedInvitationRepository(it.data, api) }
    }

    public suspend fun accept(invitationId: InvitationId) {
        base.accept(token, invitationId)
    }

    public suspend fun deny(invitationId: InvitationId) {
        base.deny(token, invitationId)
    }

    public suspend fun cancel(invitationId: InvitationId) {
        base.cancel(token, invitationId)
    }
}
