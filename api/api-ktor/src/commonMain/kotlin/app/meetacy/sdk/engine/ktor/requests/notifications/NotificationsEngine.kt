package app.meetacy.sdk.engine.ktor.requests.notifications

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.ListNotificationsRequest
import app.meetacy.sdk.engine.requests.ReadNotificationRequest
import app.meetacy.sdk.types.serializable.paging.PagingResponseSerializable
import app.meetacy.sdk.types.serializable.amount.AmountSerializable
import app.meetacy.sdk.types.serializable.amount.serializable
import app.meetacy.sdk.types.serializable.notification.NotificationIdSerializable
import app.meetacy.sdk.types.serializable.notification.NotificationSerializable
import app.meetacy.sdk.types.serializable.notification.serializable
import app.meetacy.sdk.types.serializable.notification.type
import app.meetacy.sdk.types.serializable.paging.PagingIdSerializable
import app.meetacy.sdk.types.serializable.paging.serializable
import app.meetacy.sdk.types.serializable.paging.type
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

internal class NotificationsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    private val baseUrl = baseUrl / "notifications"

    @Serializable
    private data class ListNotificationsBody(
        val amount: AmountSerializable,
        val pagingId: PagingIdSerializable?
    )
    private fun ListNotificationsRequest.toBody() = ListNotificationsBody(amount.serializable(), pagingId?.serializable())

    suspend fun list(
        request: ListNotificationsRequest
    ): ListNotificationsRequest.Response {
        val url = baseUrl / "list"
        val body = request.toBody()

        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<PagingResponseSerializable<NotificationSerializable>>()
            .type()
            .mapItems { notification -> notification.type() }

        return ListNotificationsRequest.Response(response)
    }

    @Serializable
    private data class ReadNotificationBody(val lastNotificationId: NotificationIdSerializable)
    private fun ReadNotificationRequest.toBody() = ReadNotificationBody(lastNotificationId.serializable())

    suspend fun read(request: ReadNotificationRequest): StatusTrueResponse {
        val url = baseUrl / "read"
        val body = request.toBody()
        return httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
    }
}
