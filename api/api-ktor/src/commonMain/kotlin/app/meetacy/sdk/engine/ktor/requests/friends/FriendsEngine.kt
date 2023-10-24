@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.friends

import app.meetacy.sdk.engine.ktor.handleRSocketExceptions
import app.meetacy.sdk.engine.ktor.mapToLocation
import app.meetacy.sdk.engine.ktor.mapToRegularUser
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.response.models.ListFriendsResponse
import app.meetacy.sdk.engine.ktor.response.models.Location as ModelLocation
import app.meetacy.sdk.engine.ktor.response.models.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.models.User as ModelUser
import app.meetacy.sdk.engine.requests.AddFriendRequest
import app.meetacy.sdk.engine.requests.DeleteFriendRequest
import app.meetacy.sdk.engine.requests.EmitFriendsLocationRequest
import app.meetacy.sdk.engine.requests.ListFriendsRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.UserLocationSnapshot
import io.ktor.client.*
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class FriendsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val baseUrl =  baseUrl / "friends"

    suspend fun add(request: AddFriendRequest): StatusTrueResponse {
        val url =  baseUrl / "add"

        val jsonObject = buildJsonObject {
            put("friendId", request.friendId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<StatusTrueResponse>(string)
    }

    suspend fun delete(request: DeleteFriendRequest): StatusTrueResponse {
        val url =  baseUrl / "delete"

        val jsonObject = buildJsonObject {
            put("friendId", request.friendId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<StatusTrueResponse>(string)
    }

    suspend fun list(request: ListFriendsRequest): ListFriendsRequest.Response {
        val url =  baseUrl / "list"

        val jsonObject = buildJsonObject {
            put("amount", request.amount.int.toString())
            put("pagingId", request.pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<ListFriendsResponse>(string).result

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map { user -> user.mapToRegularUser() }
        )

        return ListFriendsRequest.Response(paging)
    }

    suspend fun streamFriendsLocation(request: EmitFriendsLocationRequest) = handleRSocketExceptions(json) {
        val url = baseUrl.replaceProtocolWithWebsocket() / "location" / "stream"

        val socket = httpClient.rSocket(
            urlString = url.string,
            secure = url.protocol.isWss
        )

        val flow = socket.requestChannel(
            initPayload = request.encodeToPayload(json),
            payloads = request.selfLocation.map { location -> location.encodeToPayload() }
        ).map { payload ->
            EmitFriendsLocationRequest.Update(
                user = payload.decodeToUserLocationSnapshot(json)
            )
        }

        request.collector.emitAll(flow)
    }
}

private fun EmitFriendsLocationRequest.encodeToPayload(json: Json): Payload = buildPayload {
    val initObject = buildJsonObject {
        put("apiVersion", apiVersion.int)
        put("token", token.string)
    }

    data(json.encodeToString(initObject))
}

private fun Location.encodeToPayload(): Payload = buildPayload {
    val locationString = buildJsonObject {
        val `object` = buildJsonObject {
            put("latitude", latitude)
            put("longitude", longitude)
        }
        put("location", `object`)
    }.toString()

    data(locationString)
}

@Serializable
private data class ModelUserLocationSnapshotSerializable(
    val user: ModelUser,
    val location: ModelLocation,
    val capturedAt: String
)

private fun Payload.decodeToUserLocationSnapshot(json: Json): UserLocationSnapshot {
    val deserialized = json.decodeFromString<ModelUserLocationSnapshotSerializable>(data.readText())

    return UserLocationSnapshot(
        user = deserialized.user.mapToUser() as RegularUser,
        location = deserialized.location.mapToLocation(),
        capturedAt = DateTime(deserialized.capturedAt)
    )
}
