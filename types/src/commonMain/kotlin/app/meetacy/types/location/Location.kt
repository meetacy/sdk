package app.meetacy.types.location

public data class Location(
    public val latitude: Double,
    public val longitude: Double
) {
    public companion object {
        public val NullIsland: Location = Location(
            latitude = 0.0,
            longitude = 0.0
        )
    }
}
