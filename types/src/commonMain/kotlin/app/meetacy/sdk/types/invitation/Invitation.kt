package app.meetacy.sdk.types.invitation

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.user.User

public data class Invitation(
    val id: InvitationId,
    val expiryDate: DateTime,
    val invitedUser: User,
    val invitorUser: User,
    val meeting: Meeting,
    val isAccepted: AcceptationState
)
