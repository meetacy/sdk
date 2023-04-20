package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.users.AuthorizedUserRepository
import kotlinx.coroutines.flow.Flow

public class AuthorizedMeetingParticipantsRepository(
    private val meetingId: MeetingId,
    private val api: AuthorizedMeetacyApi
) {
    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<AuthorizedUserRepository>> = api.meetings
        .participants
        .list(meetingId, amount, pagingId)

    public suspend fun flow(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<AuthorizedUserRepository>> = api.meetings
        .participants
        .flow(meetingId, chunkSize, startPagingId, limit)
}
