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
import app.meetacy.sdk.engine.ktor.response.models.Invitation as ModelInvitation
import app.meetacy.sdk.engine.ktor.response.models.Location as ModelLocation
import app.meetacy.sdk.engine.ktor.response.models.Meeting as ModelMeeting
import app.meetacy.sdk.engine.ktor.response.models.Notification as ModelNotification
import app.meetacy.sdk.engine.ktor.response.models.User as ModelUser

internal fun ModelUser.mapToSelfUser(): SelfUser = mapToUser() as SelfUser
internal fun ModelUser.mapToRegularUser(): RegularUser = mapToUser() as RegularUser

@OptIn(UnsafeConstructor::class)
internal fun ModelUser.mapToUser(): User = if (isSelf) {
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

internal fun ModelInvitation.toInvitation(): Invitation = Invitation(
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

internal fun ModelMeeting.mapToMeeting(): Meeting = Meeting(
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
    previewParticipants = previewParticipants.map(ModelUser::mapToUser),
    avatarId = avatarId?.let(::FileId),
    visibility = when (visibility) {
        ModelMeeting.Visibility.PUBLIC -> Meeting.Visibility.Public
        ModelMeeting.Visibility.PRIVATE -> Meeting.Visibility.Private
    }
)

internal fun ModelLocation.mapToLocation(): Location =
    Location(latitude, longitude)

internal fun ModelNotification.mapToNotification(): Notification = when (this.type) {
    app.meetacy.sdk.engine.ktor.response.models.Notification.Type.SUBSCRIPTION -> Notification.Subscription(
        id = NotificationId(id),
        isNew = isNew,
        date = Date(date),
        subscriber = subscriber!!.mapToRegularUser()
    )
    app.meetacy.sdk.engine.ktor.response.models.Notification.Type.MEETING_INVITATION -> Notification.Invitation(
        id = NotificationId(id),
        isNew = isNew,
        date = Date(date),
        meeting = meeting!!.mapToMeeting(),
        inviter = inviter!!.mapToRegularUser()
    )
}