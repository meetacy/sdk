package app.meetacy.sdk.friends.location

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.EmitFriendsLocationRequest
import app.meetacy.sdk.types.auth.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

public class FriendsLocationApi(
    private val api: MeetacyApi
) {
    public fun stream(token: Token): Flow<FriendsOnMapRepository> =
        flow {
            val request = EmitFriendsLocationRequest(
                token = token,
                collector = this
            )
            api.engine.execute(request)
        }.map { response -> response.map }
}
