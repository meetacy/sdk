@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.friends

import app.meetacy.sdk.engine.ktor.*
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.serializable.user.UserIdSerializable
import app.meetacy.sdk.types.serializable.user.serializable
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.UserLocationSnapshot
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
    private val httpClient: HttpClient
) {
    private val baseUrl =  baseUrl / "friends"

    @Serializable
    private data class AddFriendBody(val friendId: UserIdSerializable)
    private fun AddFriendRequest.toBody() = AddFriendBody(friendId.serializable())

    suspend fun add(request: AddFriendRequest): StatusTrueResponse {
        val url =  baseUrl / "add"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
        return response
    }

    suspend fun delete(request: DeleteFriendRequest): app.meetacy.sdk.engine.ktor.models.StatusTrueResponse {
        val url =  baseUrl / "delete"

        val jsonObject = buildJsonObject {
            put("friendId", request.friendId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<app.meetacy.sdk.engine.ktor.models.StatusTrueResponse>(string)
    }

    suspend fun list(request: ListFriendsRequest): ListFriendsRequest.Response {
        val url =  baseUrl / "list"

        val jsonObject = buildJsonObject {
            put("amount", request.amount.int)
            put("pagingId", request.pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<app.meetacy.sdk.engine.ktor.models.ListFriendsResponse>(string).result

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
