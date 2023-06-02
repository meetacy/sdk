@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.invitation.AcceptationState
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.*
import dev.icerock.moko.network.generated.models.Invitation as GeneratedInvitation
import dev.icerock.moko.network.generated.models.Location as GeneratedLocation
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting
import dev.icerock.moko.network.generated.models.User as GeneratedUser

@OptIn(UnsafeConstructor::class)
internal fun GeneratedUser.mapToUser(): User = if (isSelf) {
    SelfUser(
        id = UserId(id),
        nickname = nickname,
        email = email?.let(::Email),
        emailVerified = emailVerified ?: error("Self user must always return emailVerified parameter"),
        avatarId = avatarId?.let(::FileId)
    )
} else {
    RegularUser(
        id = UserId(id),
        nickname = nickname,
        avatarId = avatarId?.let(::FileId),
        relationship = relationship?.mapToRelationship() ?: error("Regular user should always return relationship parameter")
    )
}

internal fun GeneratedInvitation.toInvitation(): Invitation = Invitation(
    id = identity.let(::InvitationId),
    meeting = meeting.mapToMeeting(),
    invitedUser = invitedUser.mapToUser(),
    invitorUser = invitorUser.mapToUser(),
    expiryDate = DateTime(expiryDate),
    isAccepted = when (isAccepted) {
        null -> AcceptationState.Waiting
        true -> AcceptationState.Accepted
        false -> AcceptationState.Declined
    }
)

internal fun String.mapToRelationship(): Relationship? = when(this) {
    "none" -> Relationship.None
    "subscription" -> Relationship.Subscription
    "subscriber" -> Relationship.Subscriber
    "friend" -> Relationship.Friend
    else -> null
}

internal fun GeneratedMeeting.mapToMeeting(): Meeting = Meeting(
    id = MeetingId(id),
    creator = creator.mapToUser(),
    date = Date(date),
    location = Location(
        location.latitude,
        location.longitude
    ),
    title = title,
    description = description,
    participantsCount = participantsCount,
    isParticipating = isParticipating,
    previewParticipants = previewParticipants.map(GeneratedUser::mapToUser),
    avatarId = avatarId?.let(::FileId),
    visibility = when (visibility) {
        GeneratedMeeting.Visibility.PUBLIC -> Meeting.Visibility.Public
        GeneratedMeeting.Visibility.PRIVATE -> Meeting.Visibility.Private
    }
)

internal fun GeneratedLocation.mapToLocation(): Location =
    Location(latitude, longitude)
