package app.meetacy.sdk.engine.ktor.requests.notifications

import app.meetacy.sdk.engine.ktor.mapToNotification
import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.models.ListNotificationsResponse
import app.meetacy.sdk.engine.ktor.models.StatusTrueResponse
import app.meetacy.sdk.engine.requests.ListNotificationsRequest
import app.meetacy.sdk.engine.requests.ReadNotificationRequest
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class NotificationsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = baseUrl / "notifications"

    suspend fun list(
        request: ListNotificationsRequest
    ): ListNotificationsRequest.Response = with (request) {
        val url = baseUrl / "list"

        val jsonObject = buildJsonObject {
            put("amount", amount.int)
            put("pagingId", pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<app.meetacy.sdk.engine.ktor.models.ListNotificationsResponse>(string).result

        val paging = PagingResponse(
            data = response.data.map { it.mapToNotification() },
            nextPagingId = response.nextPagingId?.let(::PagingId)
        )

        ListNotificationsRequest.Response(paging)
    }

    suspend fun read(request: ReadNotificationRequest): app.meetacy.sdk.engine.ktor.models.StatusTrueResponse = with (request) {
        val url = baseUrl / "read"

        val jsonObject = buildJsonObject {
            put("lastNotificationId", request.lastNotificationId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<app.meetacy.sdk.engine.ktor.models.StatusTrueResponse>(string)
    }
}
