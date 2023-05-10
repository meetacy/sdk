package app.meetacy.sdk.friends.location

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.users.AuthorizedRegularUserRepository

public class AuthorizedUserOnMapRepository(
    public val user: AuthorizedRegularUserRepository,
    public val location: Location,
    public val capturedAt: DateTime
)
