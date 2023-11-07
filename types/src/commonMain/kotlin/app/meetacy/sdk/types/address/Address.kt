package app.meetacy.sdk.types.address

public data class Address(
    val country: String,
    val city: String,
    val street: String,
    val placeName: String?
)
