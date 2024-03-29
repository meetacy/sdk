package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.users.AuthorizedUserRepository

public class AuthorizedMeetingParticipantsRepository(
    private val meetingId: MeetingId,
    private val api: AuthorizedMeetacyApi
) {
    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedUserRepository> = api.meetings
        .participants
        .list(meetingId, amount, pagingId)

    public suspend fun paging(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<AuthorizedUserRepository> = api.meetings
        .participants
        .paging(meetingId, chunkSize, startPagingId, limit)
}
