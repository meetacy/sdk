@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.friends

import app.meetacy.sdk.engine.ktor.handleRSocketExceptions
import app.meetacy.sdk.engine.ktor.mapToLocation
import app.meetacy.sdk.engine.ktor.mapToRegularUser
import app.meetacy.sdk.engine.ktor.mapToUser
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
import dev.icerock.moko.network.generated.apis.FriendsApi
import dev.icerock.moko.network.generated.apis.FriendsApiImpl
import dev.icerock.moko.network.generated.models.AccessFriendRequest
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
import dev.icerock.moko.network.generated.models.ListFriendsRequest as GeneratedListFriendsRequest
import dev.icerock.moko.network.generated.models.Location as GeneratedLocation
import dev.icerock.moko.network.generated.models.User as GeneratedUser

internal class FriendsEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val base: FriendsApi = FriendsApiImpl(baseUrl.string, httpClient, json)

    suspend fun add(request: AddFriendRequest) {
        base.friendsAddPost(
            accessFriendRequest = AccessFriendRequest(
                friendId = request.friendId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )
    }

    suspend fun delete(request: DeleteFriendRequest) {
        base.friendsDeletePost(
            accessFriendRequest = AccessFriendRequest(
                friendId = request.friendId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )
    }

    suspend fun list(request: ListFriendsRequest): ListFriendsRequest.Response {
        val response = base.friendsListPost(
            listFriendsRequest = GeneratedListFriendsRequest(
                amount = request.amount.int,
                pagingId = request.pagingId?.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map { user -> user.mapToRegularUser() }
        )

        return ListFriendsRequest.Response(paging)
    }

    suspend fun streamFriendsLocation(request: EmitFriendsLocationRequest) = handleRSocketExceptions(json) {
        val url = baseUrl.replaceProtocolWithWebsocket() / "friends" / "location" / "stream"

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
private data class UserLocationSnapshotSerializable(
    val user: GeneratedUser,
    val location: GeneratedLocation,
    val capturedAt: String
)

private fun Payload.decodeToUserLocationSnapshot(json: Json): UserLocationSnapshot {
    val deserialized = json.decodeFromString<UserLocationSnapshotSerializable>(data.readText())

    return UserLocationSnapshot(
        user = deserialized.user.mapToUser() as RegularUser,
        location = deserialized.location.mapToLocation(),
        capturedAt = DateTime(deserialized.capturedAt)
    )
}
