package app.meetacy.sdk.meetings.history

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import kotlinx.coroutines.flow.Flow

public class AuthorizedMeetingsHistoryApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: MeetingsHistoryApi get() = api.base.meetings.history

    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<AuthorizedMeetingRepository>> = base.list(token, amount, pagingId)

    public fun flow(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<AuthorizedMeetingRepository>> = base.flow(token, chunkSize, startPagingId, limit)
}
