package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.user.RegularUser

public data class ListFriendsRequest(
    val token: Token,
    val amount: Amount,
    val pagingId: PagingId?
) : MeetacyRequest<ListFriendsRequest.Response> {
    public data class Response(val paging: PagingResponse<RegularUser>)
}
