package app.meetacy.sdk.meetings.history

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingsHistoryRequest
import app.meetacy.sdk.meetings.MeetingRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.paging.mapItems

public class MeetingsHistoryApi(private val api: MeetacyApi) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<MeetingRepository>> = api.engine
        .execute(
            request = ListMeetingsHistoryRequest(token, amount, pagingId)
        ).paging.mapItems { meeting ->
            MeetingRepository(meeting, api)
        }

    public fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingRepository<List<MeetingRepository>> = PagingRepository(
        chunkSize,
        startPagingId,
        limit
    ) { amount, pagingId -> list(token, amount, pagingId) }
}
