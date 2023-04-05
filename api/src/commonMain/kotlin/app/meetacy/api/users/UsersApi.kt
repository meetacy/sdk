package app.meetacy.api.users

import app.meetacy.api.MeetacyApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.GetUserRequest
import app.meetacy.types.auth.Token
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId

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
