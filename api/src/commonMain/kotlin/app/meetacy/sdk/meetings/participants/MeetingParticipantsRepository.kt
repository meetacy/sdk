package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.users.UserRepository
import kotlinx.coroutines.flow.Flow

public class MeetingParticipantsRepository(
    private val meetingId: MeetingId,
    private val api: MeetacyApi
) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<UserRepository> = api.meetings
        .participants
        .list(token, meetingId, amount, pagingId)

    public suspend fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<UserRepository> = api.meetings
        .participants
        .paging(token, meetingId, chunkSize, startPagingId, limit)
}
