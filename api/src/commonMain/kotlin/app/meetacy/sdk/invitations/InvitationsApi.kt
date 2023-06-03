package app.meetacy.sdk.invitations

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.AcceptInvitationRequest
import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User

public class InvitationsApi(private val api: MeetacyApi) {
    public suspend fun create(
        token: Token,
        invitedUser: User,
        expiryDate: DateTime,
        meetingId: MeetingId
    ): InvitationsRepository {
        val invitation = api.engine.execute(
            request = CreateInvitationRequest(
                token,
                invitedUser,
                expiryDate,
                meetingId
            )
        ).invitation

        return InvitationsRepository(invitation, api)
    }

    public suspend fun accept(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(
            request = AcceptInvitationRequest(
                token, invitationId
            )
        )
    }
}
