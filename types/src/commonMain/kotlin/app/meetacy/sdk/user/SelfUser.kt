package app.meetacy.sdk.user

import app.meetacy.sdk.email.Email

public interface SelfUser : User {
    public val emailVerified: Boolean
}

public data class SelfUserImpl(
    override val id: UserId,
    override val email: Email?,
    override val emailVerified: Boolean,
) : SelfUser
