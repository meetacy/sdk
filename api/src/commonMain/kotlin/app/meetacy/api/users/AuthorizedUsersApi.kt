package app.meetacy.api.users

import app.meetacy.types.auth.Token
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId

public class AuthorizedUsersApi(
    public val token: Token,
    public val base: UsersApi
) {
    public suspend fun get(userId: UserId): User = base.get(token, userId)
}
