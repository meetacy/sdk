package app.meetacy.sdk.types.serializable.place

import app.meetacy.sdk.types.place.Place
import app.meetacy.sdk.types.serializable.address.AddressSerializable
import app.meetacy.sdk.types.serializable.address.serializable
import app.meetacy.sdk.types.serializable.address.type
import app.meetacy.sdk.types.serializable.location.LocationSerializable
import app.meetacy.sdk.types.serializable.location.serializable
import app.meetacy.sdk.types.serializable.location.type
import kotlinx.serialization.Serializable

@Serializable
public data class PlaceSerializable(
    val address: AddressSerializable,
    val location: LocationSerializable
)

public fun PlaceSerializable.type(): Place = Place(address.type(), location.type())
public fun Place.serializable(): PlaceSerializable = PlaceSerializable(address.serializable(), location.serializable())
