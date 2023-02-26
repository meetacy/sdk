package app.meetacy.api

import app.meetacy.api.auth.AuthorizedAuthApi
import app.meetacy.api.engine.updates.filter.MeetacyUpdateFilter
import app.meetacy.api.friends.AuthorizedFriendsApi
import app.meetacy.api.users.AuthorizedUsersApi
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.update.UpdateId
import app.meetacy.types.user.SelfUser

/**
 * Even though this class *seems* to be safe,
 * server can make token invalid at any time, so
 * you should check for unauthorized exceptions
 */
public class AuthorizedMeetacyApi @UnsafeConstructor constructor(
    public val token: Token,
    public val base: MeetacyApi
) {
    public val auth: AuthorizedAuthApi = AuthorizedAuthApi(token, base.auth)
    public val friends: AuthorizedFriendsApi = AuthorizedFriendsApi(token, base.friends)
    public val users: AuthorizedUsersApi = AuthorizedUsersApi(token, base.users)

    public fun updatesPolling(vararg filters: MeetacyUpdateFilter<*>, lastUpdateId: UpdateId? = null) {
        base.updatesPolling(token, *filters, lastUpdateId = lastUpdateId)
    }

    public suspend fun getMe(): SelfUser = base.getMe(token)
}
