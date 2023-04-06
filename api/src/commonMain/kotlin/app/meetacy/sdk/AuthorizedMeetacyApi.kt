package app.meetacy.sdk

import app.meetacy.sdk.files.AuthorizedFilesApi
import app.meetacy.sdk.friends.AuthorizedFriendsApi
import app.meetacy.sdk.meetings.AuthorizedMeetingsApi
import app.meetacy.sdk.users.AuthorizedSelfUserRepository
import app.meetacy.sdk.users.AuthorizedUsersApi
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token

/**
 * Even though this class *seems* to be safe,
 * server can make token invalid at any point of time, so
 * you should check for unauthorized exceptions anyway
 *
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.SelfUserRepository]
 * - [app.meetacy.sdk.users.AuthorizedSelfUserRepository]
 */
public class AuthorizedMeetacyApi @UnsafeConstructor constructor(
    public val token: Token,
    public val base: MeetacyApi
) {
    public val files: AuthorizedFilesApi = AuthorizedFilesApi(api = this)
    public val auth: app.meetacy.sdk.auth.AuthorizedAuthApi = app.meetacy.sdk.auth.AuthorizedAuthApi(api = this)
    public val friends: AuthorizedFriendsApi = AuthorizedFriendsApi(api = this)
    public val users: AuthorizedUsersApi = AuthorizedUsersApi(api = this)
    public val meetings: AuthorizedMeetingsApi = AuthorizedMeetingsApi(api = this)

    public suspend fun getMe(): AuthorizedSelfUserRepository =
        AuthorizedSelfUserRepository(
            data = base.getMe(token),
            api = this
        )
}
