package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId

public data class SelfUser(
    override val id: UserId,
    val email: Email?,
    override val nickname: String,
    val emailVerified: Boolean,
    override val username: Username?,
    override val avatarId: FileId?
) : User {
    override val isSelf: Boolean = true
    override val relationship: Relationship? = null
}
