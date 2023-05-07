package app.meetacy.sdk.friends.location

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.EmitFriendsLocationRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.users.RegularUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

public class FriendsLocationApi(
    private val api: MeetacyApi
) {
    public fun stream(
        token: Token,
        selfLocation: Flow<Location>,
    ): Flow<UserOnMapRepository> =
        flow {
            val request = EmitFriendsLocationRequest(
                token = token,
                collector = this,
                selfLocation = selfLocation
            )
            api.engine.execute(request)
        }.map { response ->
            val userOnMap = response.user

            UserOnMapRepository(
                user = RegularUserRepository(
                    data = userOnMap.user,
                    api = api
                ),
                location = userOnMap.location,
                capturedAt = userOnMap.capturedAt
            )
        }
}
