@file:Suppress("OPT_IN_OVERRIDE")

package app.meetacy.sdk.types.serializable.optional

import app.meetacy.sdk.types.optional.Optional
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Optional.Undefined is never being encoded in JSON
 */
@Serializable(with = OptionalSerializable.Serializer::class)
public sealed interface OptionalSerializable<out T> {
    public val value: T? get() = null
    public data class Present<T>(override val value: T) : OptionalSerializable<T>
    public data object Undefined : OptionalSerializable<Nothing>

    public class Serializer<T>(
        private val subSerializer: KSerializer<T>
    ) : KSerializer<OptionalSerializable<T>> {

        override val descriptor: SerialDescriptor = object : SerialDescriptor by subSerializer.descriptor {
            override val serialName = "OptionalSerializer"
            override val isNullable = true
        }

        override fun deserialize(decoder: Decoder): OptionalSerializable<T> {
            val value = decoder.decodeSerializableValue(subSerializer)
            return Present(value)
        }

        override fun serialize(encoder: Encoder, value: OptionalSerializable<T>) {
            if (value !is Present) {
                error(
                    message = "Only 'Present' values can be encoded, " +
                            "please consider to use 'Undefined' as default " +
                            "value in order to prevent it from encoding"
                )
            }

            encoder.encodeSerializableValue(subSerializer, value.value)
        }
    }
}

public fun <T> OptionalSerializable<T>.type(): Optional<T> = when (this) {
    is OptionalSerializable.Present -> Optional.Present(value)
    is OptionalSerializable.Undefined -> Optional.Undefined
}

public fun <T> Optional<T>.serializable(): OptionalSerializable<T> = when (this) {
    is Optional.Present -> OptionalSerializable.Present(value)
    is Optional.Undefined -> OptionalSerializable.Undefined
}
