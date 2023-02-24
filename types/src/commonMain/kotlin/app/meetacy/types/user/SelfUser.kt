package app.meetacy.types.user

import app.meetacy.types.email.Email

public interface SelfUser : User {
    public val emailVerified: Boolean
}

public data class SelfUserImpl(
    override val id: UserId,
    override val email: Email?,
    override val emailVerified: Boolean,
) : SelfUser
