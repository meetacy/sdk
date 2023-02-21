package app.meetacy.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
    val id: MeetingId,
    val creator: User,
    val date: Date,
    val location: Location,
    val title: String?,
    val description: String?,
    val participantsCount: Int,
    val isParticipating: Boolean,
    val avatarId: FileId?
)
