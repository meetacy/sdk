package app.meetacy.sdk.invitations

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User

public class AuthorizedInvitationsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: InvitationsApi = api.base.invitations

    public suspend fun create(invitedUser: User, expiryDate: DateTime, meetingId: MeetingId): Invitation =
        base.create(token, invitedUser, expiryDate, meetingId)
}