package app.meetacy.sdk.types.serializable.location

import app.meetacy.sdk.types.location.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LocationSerializable(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double
)

public fun LocationSerializable.type(): Location = Location(latitude, longitude)
public fun Location.serializable(): LocationSerializable = LocationSerializable(latitude, longitude)