package app.meetacy.sdk.engine.ktor.response

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.rsocket.kotlin.payload.Payload
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable(with = ServerResponse.Serializer::class)
internal sealed interface ServerResponse<out T> {
    @Serializable
    data class Error(
        val errorCode: Int,
        val errorMessage: String
    ) : ServerResponse<Nothing>

    @Serializable
    data class Success<T>(val result: T) : ServerResponse<T>

    @Suppress("UNCHECKED_CAST")
    class Serializer<T>(subSerializer: KSerializer<T>) : KSerializer<ServerResponse<T>> {
        private val baseSerializer = Data.serializer(subSerializer)
        override val descriptor = baseSerializer.descriptor

        override fun deserialize(decoder: Decoder): ServerResponse<T> {
            val data = baseSerializer.deserialize(decoder)
            return if (data.status) {
                Success(data.data as T)
            } else {
                Error(data.errorCode!!, data.errorMessage!!)
            }
        }

        override fun serialize(encoder: Encoder, value: ServerResponse<T>) {
            val data = Data(
                status = value is Success,
                data = (value as? Success)?.result,
                errorCode = (value as? Error)?.errorCode,
                errorMessage = (value as? Error)?.errorMessage
            )
            baseSerializer.serialize(encoder, data)
        }

        @Serializable
        private data class Data<T>(
            val status: Boolean,
            val data: T? = null,
            val errorCode: Int? = null,
            val errorMessage: String? = null
        )
    }
}

internal fun <T> Payload.decodeToServerResponse(
    subSerializer: KSerializer<T>
): ServerResponse<T> {
    return Json.decodeFromString(
        deserializer = ServerResponse.Serializer(subSerializer),
        string = data.readText()
    )
}

internal suspend inline fun <reified T : Any> HttpResponse.bodyAsSuccess(): T {
    return body<ServerResponse.Success<T>>().result
}
