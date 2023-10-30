package app.meetacy.sdk.types.serializable.address

import app.meetacy.sdk.types.address.Address
import kotlinx.serialization.Serializable

@Serializable
public data class AddressSerializable(
    val country: String,
    val city: String,
    val street: String,
    val placeName: String?
)

public fun AddressSerializable.type(): Address = Address(
    country,
    city,
    street,
    placeName
)

public fun Address.serializable(): AddressSerializable = AddressSerializable(
    country,
    city,
    street,
    placeName
)
