package app.meetacy.api.meetings.history

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.ListMeetingsHistoryRequest
import app.meetacy.api.internal.paging.pagingFlow
import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import kotlinx.coroutines.flow.Flow

public class HistoryApi(private val engine: MeetacyRequestsEngine) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<Meeting>> = engine.execute(
        ListMeetingsHistoryRequest(token, amount, pagingId)
    ).paging

    public fun flow(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<Meeting>> = pagingFlow(chunkSize, startPagingId, limit) { pagingId, amount ->
        list(token, amount, pagingId)
    }
}
