package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedSelfUserRepository]
 * - [AuthorizedRegularUserRepository]
 * - [AuthorizedRegularUserDetailsRepository]
 * - [AuthorizedSelfUserDetailsRepository]
 */
public class AuthorizedUsersApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: UsersApi get() = api.base.users

    public suspend fun get(userId: UserId): AuthorizedUserDetailsRepository {
        val user = base.get(token, userId)
        return AuthorizedUserDetailsRepository.of(user.data, api)
    }

    public suspend fun edit(
        nickname: String,
        username: Username?,
        avatarId: FileId?
    ): AuthorizedSelfUserRepository = edit(
        nickname = Optional.Present(nickname),
        username = Optional.Present(username),
        avatarId = Optional.Present(avatarId)
    )

    public suspend fun edit(
        nickname: Optional<String> = Optional.Undefined,
        username: Optional<Username?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): AuthorizedSelfUserRepository {
        val user = base.edit(token, nickname, username, avatarId)
        return AuthorizedSelfUserRepository(
            data = user.data,
            api = api
        )
    }

    public suspend fun usernameAvailable(username: Username): Username {
        return base.usernameAvailable(username)
    }
}
