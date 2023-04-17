package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.EditUserRequest
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedUsersApi]
 * - [RegularUserRepository]
 * - [SelfUserRepository]
 */
public class UsersApi(private val api: MeetacyApi) {
    public suspend fun get(token: Token, userId: UserId): User {
        return api.engine.execute(GetUserRequest(token, userId)).user
    }

    public suspend fun edit(
        token: Token,
        nickname: String,
        avatarId: FileId?
    ): SelfUserRepository = edit(
        token = token,
        nickname = Optional.Present(nickname),
        avatarId = Optional.Present(avatarId)
    )

    public suspend fun edit(
        token: Token,
        nickname: Optional<String> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): SelfUserRepository {
        val user = api.engine.execute(EditUserRequest(token, nickname, avatarId)).user
        return SelfUserRepository(
            data = user,
            api = api.authorized(token)
        )
    }
}
