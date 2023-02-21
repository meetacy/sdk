package app.meetacy.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class SelfUser(
    val id: UserId,
    val avatar: FileId,
    val nickname: String,
    val email: Email? = null,
    val emailVerified: Boolean? = null
)

fun SelfUser.toUser() = User(id, avatar, nickname, email, emailVerified)
