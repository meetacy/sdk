package app.meetacy.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UserId,
    val avatar: FileId,
    val nickname: String,
    val email: Email? = null,
    val emailVerified: Boolean? = null
)

fun User.toSelfUser(): SelfUser = SelfUser(id, avatar, nickname, email, emailVerified)
