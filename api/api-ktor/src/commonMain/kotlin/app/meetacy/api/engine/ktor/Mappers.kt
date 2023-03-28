@file:OptIn(UnsafeConstructor::class)

package app.meetacy.api.engine.ktor

import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.datetime.Date
import app.meetacy.types.email.Email
import app.meetacy.types.file.FileId
import app.meetacy.types.location.Location
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.meeting.MeetingId
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.SelfUser
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId
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
    date = Meeting.DateTimeInfo.parse(date),
    location = Location(
        location.latitude,
        location.longitude
    ),
    title = title,
    description = description,
    participantsCount = participantsCount,
    isParticipating = isParticipating,
    avatarIdentity = avatarIdentity?.let(::FileId)
)
