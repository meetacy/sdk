package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.user.User

public data class ListMeetingParticipantsRequest(
    val token: Token,
    val meetingId: MeetingId,
    val amount: Amount,
    val pagingId: PagingId?
) : MeetacyRequest<ListMeetingParticipantsRequest.Response> {
    public data class Response(val paging: PagingResponse<User>)
}
