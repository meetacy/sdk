package app.meetacy.types.user

import app.meetacy.types.email.Email
import app.meetacy.types.file.FileId

public data class SelfUser(
    override val id: UserId,
    val email: Email?,
    override val nickname: String,
    val emailVerified: Boolean,
    override val avatarId: FileId?
) : User
