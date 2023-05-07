package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location

public data class UserOnMap(
    val user: RegularUser,
    val location: Location,
    val capturedAt: DateTime
)
