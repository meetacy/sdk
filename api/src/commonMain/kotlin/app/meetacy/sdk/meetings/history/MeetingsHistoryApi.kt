package app.meetacy.sdk.meetings.history

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingsHistoryRequest
import app.meetacy.sdk.internal.paging.pagingFlow
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.paging.mapItems
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
