package app.meetacy.api.meetings.history

import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import kotlinx.coroutines.flow.Flow

public class AuthorizedHistoryApi(
    public val token: Token,
    public val base: HistoryApi
) {
    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<Meeting>> = base.list(token, amount, pagingId)

    public fun flow(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<Meeting>> = base.flow(token, chunkSize, startPagingId, limit)
}
