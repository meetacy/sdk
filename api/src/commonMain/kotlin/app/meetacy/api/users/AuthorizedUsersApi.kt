package app.meetacy.api.users

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.MeetacyApi
import app.meetacy.types.auth.Token
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId

/**
 *
 */
public class AuthorizedUsersApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: UsersApi get() = api.base.users

    public suspend fun get(userId: UserId): User = base.get(token, userId)
}
