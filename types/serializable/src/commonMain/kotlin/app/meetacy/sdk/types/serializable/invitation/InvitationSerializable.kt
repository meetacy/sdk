package app.meetacy.sdk.types.serializable.invitation

import app.meetacy.sdk.types.invitation.AcceptationState
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.serializable.meeting.MeetingSerializable
import app.meetacy.sdk.types.serializable.meeting.type
import app.meetacy.sdk.types.serializable.user.UserSerializable
import app.meetacy.sdk.types.serializable.user.type
import kotlinx.serialization.Serializable

@Serializable
public data class InvitationSerializable(
    val identity: InvitationIdSerializable,
    val invitedUser: UserSerializable,
    val inviterUser: UserSerializable,
    val meeting: MeetingSerializable,
    val isAccepted: Boolean? = null
)

public fun InvitationSerializable.type(): Invitation = Invitation(
    id = identity.type(),
    meeting = meeting.type(),
    invitedUser = invitedUser.type(),
    inviterUser = inviterUser.type(),
    isAccepted = when (isAccepted) {
        null -> AcceptationState.Waiting
        true -> AcceptationState.Accepted
        false -> AcceptationState.Declined
    }
)