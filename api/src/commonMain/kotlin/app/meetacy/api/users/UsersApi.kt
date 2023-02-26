package app.meetacy.api.users

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.GetUserRequest
import app.meetacy.types.auth.Token
import app.meetacy.types.user.User
import app.meetacy.types.user.UserId

public class UsersApi(private val engine: MeetacyRequestsEngine) {
    public suspend fun get(token: Token, userId: UserId): User {
        return engine.execute(GetUserRequest(token, userId)).user
    }
}
