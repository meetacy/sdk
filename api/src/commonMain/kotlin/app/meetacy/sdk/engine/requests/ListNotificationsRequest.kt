package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.notification.Notification
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse

public data class ListNotificationsRequest(
    override val token: Token,
    val amount: Amount,
    val pagingId: PagingId?
) : MeetacyRequest<ListNotificationsRequest.Response>, MeetacyRequestWithToken<ListNotificationsRequest.Response> {
    public data class Response(val paging: PagingResponse<Notification>)
}
