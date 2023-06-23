package app.meetacy.sdk.engine.ktor.requests.notifications

import app.meetacy.sdk.engine.ktor.mapToNotification
import app.meetacy.sdk.engine.requests.ListNotificationsRequest
import app.meetacy.sdk.engine.requests.ReadNotificationRequest
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.NotificationsApiImpl
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.ListNotificationsRequest as GeneratedListNotificationsRequest
import dev.icerock.moko.network.generated.models.ReadNotificationRequest as GeneratedReadNotificationRequest

internal class NotificationsEngine(
    baseUrl: Url,
    httpClient: HttpClient,
    json: Json
) {
    private val base = NotificationsApiImpl(baseUrl.string, httpClient, json)

    suspend fun listNotifications(
        request: ListNotificationsRequest
    ): ListNotificationsRequest.Response = with (request) {
        val response = base.notificationsListPost(
            apiVersion = apiVersion.int.toString(),
            listNotificationsRequest = GeneratedListNotificationsRequest(
                token = token.string,
                amount = amount.int,
                pagingId = pagingId?.string
            )
        ).result

        val paging = PagingResponse(
            data = response.data.map { it.mapToNotification() },
            nextPagingId = response.nextPagingId?.let(::PagingId)
        )

        ListNotificationsRequest.Response(paging)
    }

    suspend fun read(request: ReadNotificationRequest) = with (request) {
        base.notificationsReadPost(
            apiVersion = apiVersion.int.toString(),
            readNotificationRequest = GeneratedReadNotificationRequest(
                token = token.string,
                lastNotificationId = lastNotificationId.string
            )
        )
    }
}
