package app.meetacy.sdk.engine.requests

import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.user.RegularUser

public data class ListFriendsRequest(
    val token: Token,
    val amount: Amount,
    val pagingId: PagingId?
) : MeetacyRequest<ListFriendsRequest.Response> {
    public data class Response(val paging: PagingResponse<List<RegularUser>>)
}
