package app.meetacy.sdk.types.friends

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.user.RegularUser

public data class FriendOnMap(
    val location: Location,
    val user: RegularUser,
    val updatedAt: DateTime
)
