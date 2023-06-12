package app.meetacy.sdk.meetings.history

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListActiveMeetingsRequest
import app.meetacy.sdk.engine.requests.ListMeetingsHistoryRequest
import app.meetacy.sdk.meetings.MeetingRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource

public class MeetingsHistoryApi(private val api: MeetacyApi) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<MeetingRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = ListMeetingsHistoryRequest(token, currentAmount, currentPagingId)
        ).paging.mapItems { meeting ->
            MeetingRepository(meeting, api)
        }
    }
    public suspend fun active(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<MeetingRepository> = PagingRepository(
        amount, pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = ListActiveMeetingsRequest(token, currentAmount, currentPagingId)
        ).paging.mapItems { meeting ->
            MeetingRepository(meeting, api)
        }
    }

    public fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<MeetingRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            list(token, currentAmount, currentPagingId).response
        }
    }
}
