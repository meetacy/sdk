package app.meetacy.sdk.types.place

import app.meetacy.sdk.types.address.Address
import app.meetacy.sdk.types.location.Location

public data class Place(
    val address: Address,
    val location: Location
)
