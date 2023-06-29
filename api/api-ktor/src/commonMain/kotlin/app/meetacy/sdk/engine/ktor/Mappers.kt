@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.invitation.AcceptationState
import app.meetacy.sdk.types.invitation.Invitation
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.types.user.*
import dev.icerock.moko.network.generated.models.Notification.Type.MEETING_INVITATION
import dev.icerock.moko.network.generated.models.Notification.Type.SUBSCRIPTION
import dev.icerock.moko.network.generated.models.Invitation as GeneratedInvitation
import dev.icerock.moko.network.generated.models.Location as GeneratedLocation
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting
import dev.icerock.moko.network.generated.models.Notification as GeneratedNotification
import dev.icerock.moko.network.generated.models.User as GeneratedUser

internal fun GeneratedUser.mapToSelfUser(): SelfUser = mapToUser() as SelfUser
internal fun GeneratedUser.mapToRegularUser(): RegularUser = mapToUser() as RegularUser

@OptIn(UnsafeConstructor::class)
internal fun GeneratedUser.mapToUser(): User = if (isSelf) {
    SelfUser(
        id = UserId(id),
        nickname = nickname,
        email = email?.let(::Email),
        emailVerified = emailVerified ?: error("Self user must always return emailVerified parameter"),
        username = username?.let(::Username),
        avatarId = avatarId?.let(::FileId)
    )
} else {
    RegularUser(
        id = UserId(id),
        nickname = nickname,
        avatarId = avatarId?.let(::FileId),
        username = username?.let(::Username),
        relationship = relationship?.mapToRelationship() ?: error("Regular user should always return relationship parameter")
    )
}

internal fun GeneratedInvitation.toInvitation(): Invitation = Invitation(
    id = identity.let(::InvitationId),
    meeting = meeting.mapToMeeting(),
    invitedUser = invitedUser.mapToUser(),
    inviterUser = inviterUser.mapToUser(),
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

internal fun GeneratedNotification.mapToNotification(): Notification = when (this.type) {
    SUBSCRIPTION -> Notification.Subscription(
        id = NotificationId(id),
        isNew = isNew,
        date = Date(date),
        subscriber = subscriber!!.mapToRegularUser()
    )
    MEETING_INVITATION -> Notification.Invitation(
        id = NotificationId(id),
        isNew = isNew,
        date = Date(date),
        meeting = meeting!!.mapToMeeting(),
        inviter = inviter!!.mapToRegularUser()
    )
}
