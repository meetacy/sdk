package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.UserId

public data class GetSubscribersRequest(
    val token: Token,
    val amount: Amount,
    val pagingId: PagingId?,
    val userId: UserId?,
) : MeetacyRequest<GetSubscriptionsRequest.Response> {
    public data class Response(val paging: PagingResponse<RegularUser>)
}
