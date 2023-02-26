package app.meetacy.types.user

import app.meetacy.types.email.Email

public data class SelfUser(
    override val id: UserId,
    override val email: Email?,
    val emailVerified: Boolean,
) : User
