package app.meetacy.api

import app.meetacy.api.auth.AuthApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.GetMeRequest
import app.meetacy.api.friends.FriendsApi
import app.meetacy.api.meetings.MeetingsApi
import app.meetacy.api.users.UsersApi
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.user.SelfUser

public class MeetacyApi(private val engine: MeetacyRequestsEngine) {
    public val auth: AuthApi = AuthApi(engine, meetacyApi = this)
    public val friends: FriendsApi = FriendsApi(engine)
    public val users: UsersApi = UsersApi(engine)
    public val meetings: MeetingsApi = MeetingsApi(engine)

    public suspend fun getMe(token: Token): SelfUser {
        return engine.execute(GetMeRequest(token)).me
    }

    @OptIn(UnsafeConstructor::class)
    public fun authorized(token: Token): AuthorizedMeetacyApi {
        return AuthorizedMeetacyApi(token, base = this)
    }

    public companion object
}
