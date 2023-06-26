package app.meetacy.sdk.friends.location

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.users.AuthorizedRegularUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class AuthorizedFriendsLocationApi(
    private val api: AuthorizedMeetacyApi
) {
    public val token: Token get() = api.token
    public val base: FriendsLocationApi get() = api.base.friends.location

    public fun flow(selfLocation: Flow<Location>): Flow<AuthorizedUserLocationSnapshotRepository> {
        return api.base.friends.location
            .flow(api.token, selfLocation)
            .map { userOnMap ->
                AuthorizedUserLocationSnapshotRepository(
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
