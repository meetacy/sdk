package app.meetacy.sdk.user

import app.meetacy.sdk.email.Email

public interface User {
    public val id: UserId
    public val email: Email?
}

public data class UserImpl(
    override val id: UserId,
    override val email: Email?
) : User
