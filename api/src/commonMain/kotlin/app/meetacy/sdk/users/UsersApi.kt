package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.GetUserRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedUsersApi]
 * - [RegularUserRepository]
 */
public class UsersApi(private val api: MeetacyApi) {
    public suspend fun get(token: Token, userId: UserId): User {
        return api.engine.execute(GetUserRequest(token, userId)).user
    }
}
