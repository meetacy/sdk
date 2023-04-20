package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingParticipantsRequest
import app.meetacy.sdk.internal.paging.pagingFlow
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.users.UserRepository
import kotlinx.coroutines.flow.Flow

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

    public suspend fun flow(
        token: Token,
        meetingId: MeetingId,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<UserRepository>> = pagingFlow(chunkSize, startPagingId, limit) { pagingId, currentPagingId ->
        list(token, meetingId, currentPagingId, pagingId)
    }
}
