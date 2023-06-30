package app.meetacy.sdk.friends.location

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.users.RegularUserRepository

public data class UserLocationSnapshotRepository(
    public val user: RegularUserRepository,
    public val location: Location,
    public val capturedAt: DateTime
)
