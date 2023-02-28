package app.meetacy.types.user

import app.meetacy.types.email.Email

public data class SelfUser(
    override val id: UserId,
    override val email: Email?,
    override val nickname: String,
    val emailVerified: Boolean,
) : User
