package app.meetacy.sdk.friends.location

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.friends.FriendsApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.user.UserOnMap
import app.meetacy.sdk.users.AuthorizedRegularUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class AuthorizedFriendsLocationApi(
    private val api: AuthorizedMeetacyApi
) {
    public val token: Token get() = api.token
    public val base: FriendsApi get() = api.base.friends

    public fun stream(selfLocation: Flow<Location>): Flow<AuthorizedUserOnMapRepository> {
        return api.base.friends.location
            .stream(api.token, selfLocation)
            .map { userOnMap ->
                AuthorizedUserOnMapRepository(
                    user = AuthorizedRegularUserRepository(
                        data = userOnMap.user.data,
                        api = api
                    ),
                    location = userOnMap.location,
                    capturedAt = userOnMap.capturedAt
                )
            }
    }
}
