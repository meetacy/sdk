package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

/**
 *
 */
public class AuthorizedUsersApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: UsersApi get() = api.base.users

    public suspend fun get(userId: UserId): User = base.get(token, userId)
}
