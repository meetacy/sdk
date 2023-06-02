package app.meetacy.sdk.invitations

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User

public class InvitationsApi(private val api: MeetacyApi) {
    public suspend fun create(token: Token, invitedUser: User, expiryDate: DateTime, meetingId: MeetingId): Invitation {
        return api.engine.execute(CreateInvitationRequest(token, invitedUser, expiryDate, meetingId)).invitation
    }
}
