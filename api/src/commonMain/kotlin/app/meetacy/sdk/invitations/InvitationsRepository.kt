package app.meetacy.sdk.invitations

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.user.User

public class InvitationsRepository(
    public val data: Invitation,
    private val api: MeetacyApi
) {
    public val id: InvitationId get() = data.id
    public val expiryDate: DateTime get() = data.expiryDate
    public val invitedUser: User get() = data.invitedUser
    public val invitorUser: User get() = data.invitorUser
    public val meeting: Meeting get() = data.meeting

    public suspend fun accept(token: Token) {
        api.invitations.accept(token, id)
    }
}
