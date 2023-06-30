package app.meetacy.sdk.types.invitation

import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

public data class Invitation(
    val id: InvitationId,
    val invitedUser: User,
    val inviterUser: User,
    val meeting: Meeting,
    val isAccepted: AcceptationState
)
