package app.meetacy.api.engine.requests

import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse

public data class ListMeetingsHistoryRequest(
    val token: Token,
    val amount: Amount,
    val pagingId: PagingId?
) : MeetacyRequest<ListMeetingsHistoryRequest.Response> {
    public data class Response(val paging: PagingResponse<List<Meeting>>)
}
