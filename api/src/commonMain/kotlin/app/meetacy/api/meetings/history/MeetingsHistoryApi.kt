package app.meetacy.api.meetings.history

import app.meetacy.api.MeetacyApi
import app.meetacy.api.engine.requests.ListMeetingsHistoryRequest
import app.meetacy.api.internal.paging.pagingFlow
import app.meetacy.api.meetings.AuthorizedMeetingRepository
import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.paging.mapItems
import kotlinx.coroutines.flow.Flow

public class MeetingsHistoryApi(private val api: MeetacyApi) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<AuthorizedMeetingRepository>> = api.engine
        .execute(
            request = ListMeetingsHistoryRequest(token, amount, pagingId)
        ).paging.mapItems { meeting ->
            AuthorizedMeetingRepository(meeting, api.authorized(token))
        }

    public fun flow(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<AuthorizedMeetingRepository>> =
        pagingFlow(chunkSize, startPagingId, limit) { pagingId, amount ->
            list(token, amount, pagingId)
        }
}
