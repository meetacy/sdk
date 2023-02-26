package app.meetacy.api

import app.meetacy.api.auth.AuthApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.GetMeRequest
import app.meetacy.api.engine.updates.MeetacyUpdate
import app.meetacy.api.engine.updates.filter.MeetacyUpdateFilter
import app.meetacy.api.friends.FriendsApi
import app.meetacy.api.users.UsersApi
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.update.UpdateId
import app.meetacy.types.user.SelfUser
import kotlinx.coroutines.flow.Flow

public class MeetacyApi(private val engine: MeetacyRequestsEngine) {
    public val auth: AuthApi = AuthApi(engine, meetacyApi = this)
    public val friends: FriendsApi = FriendsApi(engine)
    public val users: UsersApi = UsersApi(engine)

    public suspend fun getMe(token: Token): SelfUser {
        return engine.execute(GetMeRequest(token)).me
    }

    public fun updatesPolling(
        token: Token,
        vararg filters: MeetacyUpdateFilter<*>,
        lastUpdateId: UpdateId? = null
    ): Flow<MeetacyUpdate> = engine.updatesPolling(token, *filters, lastUpdateId = lastUpdateId)

    @OptIn(UnsafeConstructor::class)
    public fun authorized(token: Token): AuthorizedMeetacyApi {
        return AuthorizedMeetacyApi(token, base = this)
    }
}
