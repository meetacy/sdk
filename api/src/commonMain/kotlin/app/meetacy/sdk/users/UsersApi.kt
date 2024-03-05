package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.EditUserRequest
import app.meetacy.sdk.engine.requests.GetUserByIdRequest
import app.meetacy.sdk.engine.requests.UsernameAvailableRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedUsersApi]
 * - [RegularUserRepository]
 * - [SelfUserRepository]
 * - [RegularUserDetailsRepository]
 * - [SelfUserDetailsRepository]
 */
public class UsersApi(private val api: MeetacyApi) {
    public suspend fun get(token: Token, userId: UserId): UserDetailsRepository {
        val user = api.engine.execute(GetUserByIdRequest(token, userId)).user
        return UserDetailsRepository.of(user, api)
    }

    public suspend fun edit(
        token: Token,
        nickname: String,
        username: Username?,
        avatarId: FileId?
    ): SelfUserRepository = edit(
        token = token,
        nickname = Optional.Present(nickname),
        username = Optional.Present(username),
        avatarId = Optional.Present(avatarId)
    )

    public suspend fun edit(
        token: Token,
        nickname: Optional<String> = Optional.Undefined,
        username: Optional<Username?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): SelfUserRepository {
        val user = api.engine.execute(EditUserRequest(token, nickname, username, avatarId)).user

        return SelfUserRepository(
            data = user,
            api = api
        )
    }

    public suspend fun usernameAvailable(username: Username): Username {
        val result = api.engine.execute(UsernameAvailableRequest(username))
        return result.username
    }
}
