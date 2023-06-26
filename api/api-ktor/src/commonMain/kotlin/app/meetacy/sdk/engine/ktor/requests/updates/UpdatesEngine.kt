package app.meetacy.sdk.engine.ktor.requests.updates

import app.meetacy.sdk.engine.ktor.mapToNotification
import app.meetacy.sdk.engine.requests.EmitUpdatesRequest
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import dev.icerock.moko.network.generated.models.Notification as GeneratedNotification

internal class UpdatesEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    suspend fun stream(request: EmitUpdatesRequest) {
        val url = baseUrl.replaceProtocolWithWebsocket() / "friends" / "location" / "stream"

        val socket = httpClient.rSocket(
            urlString = url.string,
            secure = url.protocol.isWss
        )

        val flow = socket.requestStream(
            payload = request.encodeToPayload()
        ).map { payload ->
            EmitUpdatesRequest.Update(
                update = payload.decodeToUpdate(json)
            )
        }

        request.collector.emitAll(flow)
    }
}

private fun EmitUpdatesRequest.encodeToPayload(): Payload = buildPayload {
    val initString = buildJsonObject {
        put("token", token.string)
        put("apiVersion", apiVersion.int)
    }.toString()

    data(initString)
}

@Serializable
private sealed interface UpdateSerializable {
    val id: String

    data class Notification(
        override val id: String,
        val notification: GeneratedNotification
    ) : UpdateSerializable
}

private fun Payload.decodeToUpdate(json: Json): Update {
    return when (
        val deserialized = json.decodeFromString<UpdateSerializable>(data.readText())
    ) {
        is UpdateSerializable.Notification -> Update.Notification(
            id = UpdateId(deserialized.id),
            notification = deserialized.notification.mapToNotification()
        )
    }
}
