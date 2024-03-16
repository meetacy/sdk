package app.meetacy.sdk.engine.ktor.requests.friends.subscribers

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.ListSubscribersRequest
import app.meetacy.sdk.types.serializable.paging.PagingResponseSerializable
import app.meetacy.sdk.types.serializable.paging.type
import app.meetacy.sdk.types.serializable.user.UserDetailsSerializable
import app.meetacy.sdk.types.serializable.user.UserSerializable
import app.meetacy.sdk.types.serializable.user.type
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.user.User
import io.ktor.client.*
import io.ktor.client.request.*

internal class SubscribersEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    private val baseUrl = baseUrl / "subscribers"

    suspend fun list(request: ListSubscribersRequest): ListSubscribersRequest.Response {
        val url =  baseUrl / "list"

        val response = httpClient.get(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            parameter("userId", request.userId?.string)
            parameter("amount", request.amount.int)
            parameter("pagingId", request.pagingId?.string)
        }.bodyAsSuccess<PagingResponseSerializable<UserSerializable>>()
            .type()
            .mapItems(UserSerializable::type)

        return ListSubscribersRequest.Response(response)
    }
}