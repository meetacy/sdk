package app.meetacy.types.user

import app.meetacy.types.email.Email

public interface User {
    public val id: UserId
    public val email: Email?
}
