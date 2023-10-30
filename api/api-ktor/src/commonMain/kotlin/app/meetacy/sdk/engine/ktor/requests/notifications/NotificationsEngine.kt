package app.meetacy.sdk.engine.ktor.requests.notifications

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.ListMeetingsResponse
import app.meetacy.sdk.engine.ktor.response.ListNotificationsResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.CancelInvitationRequest
import app.meetacy.sdk.engine.requests.ListNotificationsRequest
import app.meetacy.sdk.engine.requests.ReadNotificationRequest
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.serializable.amount.AmountSerializable
import app.meetacy.sdk.types.serializable.amount.serializable
import app.meetacy.sdk.types.serializable.invitation.InvitationIdSerializable
import app.meetacy.sdk.types.serializable.invitation.serializable
import app.meetacy.sdk.types.serializable.paging.PagingIdSerializable
import app.meetacy.sdk.types.serializable.paging.serializable
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
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
        }.bodyAsSuccess<ListNotificationsResponse>()

        val paging = PagingResponse(
            data = response.data.map { it },
            nextPagingId = response.nextPagingId?.let(::PagingId)
        )

        return ListNotificationsRequest.Response(paging)
    }

    suspend fun read(request: ReadNotificationRequest): app.meetacy.sdk.engine.ktor.models.StatusTrueResponse = with (request) {
        val url = baseUrl / "read"
    }
}
