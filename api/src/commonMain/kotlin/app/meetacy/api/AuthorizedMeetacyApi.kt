package app.meetacy.api

import app.meetacy.api.auth.AuthorizedAuthApi
import app.meetacy.api.files.AuthorizedFilesApi
import app.meetacy.api.files.FilesApi
import app.meetacy.api.friends.AuthorizedFriendsApi
import app.meetacy.api.meetings.AuthorizedMeetingsApi
import app.meetacy.api.users.AuthorizedSelfUserRepository
import app.meetacy.api.users.AuthorizedUsersApi
import app.meetacy.api.users.SelfUserRepository
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.user.SelfUser

/**
 * Even though this class *seems* to be safe,
 * server can make token invalid at any point of time, so
 * you should check for unauthorized exceptions anyway
 *
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.api.users.SelfUserRepository]
 * - [app.meetacy.api.users.AuthorizedSelfUserRepository]
 */
public class AuthorizedMeetacyApi @UnsafeConstructor constructor(
    public val token: Token,
    public val base: MeetacyApi
) {
    public val files: AuthorizedFilesApi = AuthorizedFilesApi(api = this)
    public val auth: AuthorizedAuthApi = AuthorizedAuthApi(api = this)
    public val friends: AuthorizedFriendsApi = AuthorizedFriendsApi(api = this)
    public val users: AuthorizedUsersApi = AuthorizedUsersApi(api = this)
    public val meetings: AuthorizedMeetingsApi = AuthorizedMeetingsApi(api = this)

    public suspend fun getMe(): AuthorizedSelfUserRepository =
        AuthorizedSelfUserRepository(
            data = base.getMe(token),
            api = this
        )
}
