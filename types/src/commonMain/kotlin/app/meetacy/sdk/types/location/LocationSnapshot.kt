package app.meetacy.sdk.types.location

import app.meetacy.sdk.types.datetime.DateTime

public data class LocationSnapshot(
    val latitude: Double,
    val longitude: Double,
    val capturedAt: DateTime
) {
    public constructor(
        location: Location,
        capturedAt: DateTime
    ) : this(
        latitude = location.latitude,
        longitude = location.longitude,
        capturedAt = capturedAt
    )

    public val location: Location get() = Location(latitude, longitude)
}
