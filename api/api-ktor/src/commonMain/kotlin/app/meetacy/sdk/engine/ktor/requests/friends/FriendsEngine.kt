@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.friends

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.handleRSocketExceptions
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.serializable.amount.AmountSerializable
import app.meetacy.sdk.types.serializable.amount.serializable
import app.meetacy.sdk.types.serializable.location.LocationSerializable
import app.meetacy.sdk.types.serializable.location.type
import app.meetacy.sdk.types.serializable.paging.PagingIdSerializable
import app.meetacy.sdk.types.serializable.paging.PagingResponseSerializable
import app.meetacy.sdk.types.serializable.paging.serializable
import app.meetacy.sdk.types.serializable.paging.type
import app.meetacy.sdk.types.serializable.user.*
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.UserLocationSnapshot
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
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
    private val rsocketClient: HttpClient,
    private val json: Json
) {
    private val baseUrl =  baseUrl / "friends"

    @Serializable
    private data class AddFriendBody(val friendId: UserIdSerializable)
    private fun AddFriendRequest.toBody() = AddFriendBody(friendId.serializable())

    suspend fun add(request: AddFriendRequest) {
        val url =  baseUrl / "add"
        val body = request.toBody()
        httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
    }

    @Serializable
    private data class DeleteFriendBody(val friendId: UserIdSerializable)
    private fun DeleteFriendRequest.toBody() = DeleteFriendBody(friendId.serializable())

    suspend fun delete(request: DeleteFriendRequest) {
        val url =  baseUrl / "delete"
        val body = request.toBody()
        httpClient.delete(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
    }

    @Serializable
    private data class ListFriendsBody(
        val amount: AmountSerializable,
        val pagingId: PagingIdSerializable?
    )
    private fun ListFriendsRequest.toBody() = ListFriendsBody(amount.serializable(), pagingId?.serializable() )

    suspend fun list(request: ListFriendsRequest): ListFriendsRequest.Response {
        val url =  baseUrl / "list"
        val body = request.toBody()

        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<PagingResponseSerializable<UserSerializable>>()
            .type()
            .mapItems { user -> user.type() as RegularUser }

        return ListFriendsRequest.Response(response)
    }

    suspend fun subscriptions(request: GetSubscriptionsRequest): GetSubscriptionsRequest.Response {
        val url =  baseUrl / "subscriptions"

        val response = httpClient.get(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            parameter("id", request.userId?.string)
            parameter("amount", request.amount.int)
            parameter("pagingId", request.pagingId?.string)
        }.bodyAsSuccess<PagingResponseSerializable<UserDetailsSerializable>>()
            .type()
            .mapItems { userDetails -> userDetails.type() }

        return GetSubscriptionsRequest.Response(response)
    }

    suspend fun subscribers(request: GetSubscribersRequest): GetSubscribersRequest.Response {
        val url =  baseUrl / "subscribers"

        val response = httpClient.get(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            parameter("id", request.userId?.string)
            parameter("amount", request.amount.int)
            parameter("pagingId", request.pagingId?.string)
        }.bodyAsSuccess<PagingResponseSerializable<UserDetailsSerializable>>()
            .type()
            .mapItems { userDetails -> userDetails.type() }

        return GetSubscribersRequest.Response(response)
    }


    suspend fun streamFriendsLocation(request: EmitFriendsLocationRequest) = handleRSocketExceptions(json) {
        val url = baseUrl.replaceProtocolWithWebsocket() / "location" / "stream"

        val socket = rsocketClient.rSocket(
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
private data class UserLocationSnapshotSerializable(
    val user: UserSerializable,
    val location: LocationSerializable,
    val capturedAt: String
)

private fun Payload.decodeToUserLocationSnapshot(json: Json): UserLocationSnapshot {
    val deserialized = json.decodeFromString<UserLocationSnapshotSerializable>(data.readText())

    return UserLocationSnapshot(
        user = deserialized.user.type() as RegularUser,
        location = deserialized.location.type(),
        capturedAt = DateTime(deserialized.capturedAt)
    )
}
