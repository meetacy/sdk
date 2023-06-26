package app.meetacy.sdk.invitations

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.UserRepository
import kotlin.jvm.JvmName

public class InvitationsApi(private val api: MeetacyApi) {
    public suspend fun create(
        token: Token,
        userId: UserId,
        meetingId: MeetingId
    ): InvitationsRepository {
        val invitation = api.engine.execute(
            request = CreateInvitationRequest(
                token,
                userId,
                meetingId
            )
        ).invitation

        return InvitationsRepository(invitation, api)
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
