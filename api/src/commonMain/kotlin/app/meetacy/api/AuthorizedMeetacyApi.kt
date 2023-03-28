package app.meetacy.api

import app.meetacy.api.auth.AuthorizedAuthApi
import app.meetacy.api.friends.AuthorizedFriendsApi
import app.meetacy.api.meetings.AuthorizedMeetingsApi
import app.meetacy.api.users.AuthorizedUsersApi
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.update.UpdateId
import app.meetacy.types.user.SelfUser

/**
 * Even though this class *seems* to be safe,
 * server can make token invalid at any point of time, so
 * you should check for unauthorized exceptions anyway
 */
public class AuthorizedMeetacyApi @UnsafeConstructor constructor(
    public val token: Token,
    public val base: MeetacyApi
) {
    public val auth: AuthorizedAuthApi = AuthorizedAuthApi(token, base.auth)
    public val friends: AuthorizedFriendsApi = AuthorizedFriendsApi(token, base.friends)
    public val users: AuthorizedUsersApi = AuthorizedUsersApi(token, base.users)
    public val meetings: AuthorizedMeetingsApi = AuthorizedMeetingsApi(token, base.meetings)

    public suspend fun getMe(): SelfUser = base.getMe(token)
}
