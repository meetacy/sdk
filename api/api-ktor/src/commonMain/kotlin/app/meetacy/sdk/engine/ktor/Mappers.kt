@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.datetime.DateOrTime
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting
import dev.icerock.moko.network.generated.models.User as GeneratedUser

internal fun GeneratedUser.mapToUser(): User = if (isSelf) {
    SelfUser(
        id = UserId(identity),
        nickname = nickname,
        email = email?.let(::Email),
        emailVerified = emailVerified ?: error("Self user must always return emailVerified parameter"),
        avatarId = avatarIdentity?.let(::FileId)
    )
} else {
    RegularUser(
        id = UserId(identity),
        nickname = nickname,
        avatarId = avatarIdentity?.let(::FileId)
    )
}

internal fun GeneratedMeeting.mapToMeeting(): Meeting = Meeting(
    id = MeetingId(identity),
    creator = when (creator.isSelf) {
        true -> SelfUser(
            id = UserId(creator.identity),
            email = creator.email?.let(::Email),
            nickname = creator.nickname,
            emailVerified = creator.emailVerified!!,
            avatarId = creator.avatarIdentity?.let(::FileId)
        )
        false -> RegularUser(
            id = UserId(creator.identity),
            nickname = creator.nickname,
            avatarId = creator.avatarIdentity?.let(::FileId)
        )
    },
    date = DateOrTime.parse(date),
    location = Location(
        location.latitude,
        location.longitude
    ),
    title = title,
    description = description,
    participantsCount = participantsCount,
    isParticipating = isParticipating,
    previewParticipants = previewParticipants.map(GeneratedUser::mapToUser),
    avatarId = avatarIdentity?.let(::FileId),
    visibility = when (visibility) {
        GeneratedMeeting.Visibility.PUBLIC -> Meeting.Visibility.Public
        GeneratedMeeting.Visibility.PRIVATE -> Meeting.Visibility.Private
    }
)
