package app.meetacy.sdk.engine.ktor.requests.updates

import app.meetacy.sdk.engine.ktor.handleRSocketExceptions
import app.meetacy.sdk.engine.requests.EmitUpdatesRequest
import app.meetacy.sdk.types.serializable.notification.NotificationSerializable
import app.meetacy.sdk.types.serializable.notification.type
import app.meetacy.sdk.types.update.Update
import app.meetacy.sdk.types.update.UpdateId
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class UpdatesEngine(
    private val baseUrl: Url,
    private val rsocketClient: HttpClient,
    private val json: Json
) {
    suspend fun stream(request: EmitUpdatesRequest) = handleRSocketExceptions(json) {
        val url = baseUrl.replaceProtocolWithWebsocket() / "updates" / "stream"

        val socket = rsocketClient.rSocket(
            urlString = url.string,
            secure = url.protocol.isWss
        )

        val flow = socket.requestStream(
            payload = request.encodeToPayload(json)
        ).map { payload ->
            EmitUpdatesRequest.Update(
                update = payload.decodeToUpdate(json)
            )
        }

        request.collector.emitAll(flow)
    }
}

private fun EmitUpdatesRequest.encodeToPayload(json: Json): Payload = buildPayload {
    val initObject = buildJsonObject {
        put("token", token.string)
        put("fromId", fromId?.string)
        put("apiVersion", apiVersion.int)
    }

    data(json.encodeToString(initObject))
}

@Serializable
private sealed interface UpdateSerializable {
    val id: String

    @SerialName("notification")
    @Serializable
    data class Notification(
        override val id: String,
        val notification: NotificationSerializable
    ) : UpdateSerializable
}

private fun Payload.decodeToUpdate(json: Json): Update {
    return when (
        val deserialized = json.decodeFromString<UpdateSerializable>(data.readText().apply { println("STRING: $this") })
    ) {
        is UpdateSerializable.Notification -> Update.Notification(
            id = UpdateId(deserialized.id),
            notification = deserialized.notification.type()
        )
    }
}
