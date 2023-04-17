package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedSelfUserRepository]
 * - [AuthorizedRegularUserRepository]
 */
public class AuthorizedUsersApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: UsersApi get() = api.base.users

    public suspend fun get(userId: UserId): User = base.get(token, userId)

    public suspend fun edit(
        nickname: String,
        avatarId: FileId?
    ): SelfUser = base.edit(token, nickname, avatarId)

    public suspend fun edit(
        nickname: Optional<String> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): SelfUser = base.edit(token, nickname, avatarId)
}
