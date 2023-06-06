@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.*
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting
import dev.icerock.moko.network.generated.models.User as GeneratedUser

internal fun GeneratedUser.mapToSelfUser(): SelfUser = mapToUser() as SelfUser
internal fun GeneratedUser.mapToRegularUser(): RegularUser = mapToUser() as RegularUser

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
        username = username?.let(::Username),
        avatarId = avatarId?.let(::FileId)
    )
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
