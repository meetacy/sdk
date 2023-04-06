package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse

public data class ListMeetingsHistoryRequest(
    val token: Token,
    val amount: Amount,
    val pagingId: PagingId?
) : MeetacyRequest<ListMeetingsHistoryRequest.Response> {
    public data class Response(val paging: PagingResponse<List<Meeting>>)
}
