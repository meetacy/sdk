package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingParticipantsRequest
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.users.UserRepository

public class MeetingParticipantsApi(private val api: MeetacyApi) {
    public suspend fun list(
        token: Token,
        meetingId: MeetingId,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<UserRepository>> {
        val paging = api.engine.execute(
            request = ListMeetingParticipantsRequest(
                token = token,
                meetingId = meetingId,
                amount = amount,
                pagingId = pagingId
            )
        ).paging

        return paging.map { users ->
            users.map { user ->
                UserRepository.of(token, user, api)
            }
        }
    }

    public suspend fun paging(
        token: Token,
        meetingId: MeetingId,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingRepository<List<UserRepository>> = PagingRepository(
        chunkSize = chunkSize,
        startPagingId = startPagingId,
        limit = limit
    ) { amount, pagingId -> list(token, meetingId, amount, pagingId) }
}
