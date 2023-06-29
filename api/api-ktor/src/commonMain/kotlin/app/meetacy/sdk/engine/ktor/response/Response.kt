package app.meetacy.sdk.engine.ktor.response

import io.rsocket.kotlin.payload.Payload
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
internal data class ServerResponseData<T>(
    val status: Boolean,
    val data: T? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null
)

internal typealias EmptyServerResponse = ServerResponse<Nothing?>

@Serializable(with = ServerResponse.Serializer::class)
internal sealed interface ServerResponse<out T> {
    @Serializable
    data class Error(
        val errorCode: Int,
        val errorMessage: String
    ) : ServerResponse<Nothing>

    @Serializable
    data class Success<T>(val data: T) : ServerResponse<T>

    @Suppress("UNCHECKED_CAST")
    class Serializer<T>(subSerializer: KSerializer<T>) : KSerializer<ServerResponse<T>> {
        private val baseSerializer = ServerResponseData.serializer(subSerializer)

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
            val data = ServerResponseData(
                status = value is Success,
                data = (value as? Success)?.data,
                errorCode = (value as? Error)?.errorCode,
                errorMessage = (value as? Error)?.errorMessage
            )
            baseSerializer.serialize(encoder, data)
        }

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

@OptIn(ExperimentalSerializationApi::class)
internal fun Payload.decodeToEmptyServerResponse(): EmptyServerResponse {
    return decodeToServerResponse(NothingSerializer().nullable)
}
