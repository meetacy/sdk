package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.location.LocationSnapshot

public data class UserLocationSnapshot(
    val user: RegularUser,
    val location: Location,
    val capturedAt: DateTime
) {
    public constructor(
        user: RegularUser,
        locationSnapshot: LocationSnapshot
    ) : this(
        user = user,
        location = locationSnapshot.location,
        capturedAt = locationSnapshot.capturedAt
    )
}
