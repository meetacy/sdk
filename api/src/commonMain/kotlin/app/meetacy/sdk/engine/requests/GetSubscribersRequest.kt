package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.user.UserDetails
import app.meetacy.sdk.types.user.UserId

public data class GetSubscribersRequest(
    val token: Token,
    val amount: Amount,
    val pagingId: PagingId?,
    val userId: UserId?,
) : MeetacyRequest<GetSubscribersRequest.Response> {
    public data class Response(val paging: PagingResponse<UserDetails>)
}
