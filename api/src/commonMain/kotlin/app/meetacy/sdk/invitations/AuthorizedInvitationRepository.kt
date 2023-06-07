package app.meetacy.sdk.invitations

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.user.User

public class AuthorizedInvitationRepository(
    public val data: Invitation,
    private val api: AuthorizedMeetacyApi
) {
    private val base: AuthorizedInvitationsApi get() = AuthorizedInvitationsApi(api)

    public val id: InvitationId get() = data.id
    public val expiryDate: DateTime get() = data.expiryDate
    public val invitedUser: User get() = data.invitedUser
    public val invitorUser: User get() = data.invitorUser
    public val meeting: Meeting get() = data.meeting

    public suspend fun accept() {
        base.accept(id)
    }
}