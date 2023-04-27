package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
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
    ): PagingResponse<List<UserRepository>> = api.meetings
        .participants
        .list(token, meetingId, amount, pagingId)

    public suspend fun flow(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<UserRepository>> = api.meetings
        .participants
        .flow(token, meetingId, chunkSize, startPagingId, limit)
}
