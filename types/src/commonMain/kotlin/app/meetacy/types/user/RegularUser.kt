package app.meetacy.types.user

import app.meetacy.types.email.Email

public data class RegularUser(
    override val id: UserId,
    override val email: Email? = null,
    override val nickname: String
) : User
