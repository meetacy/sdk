package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListMeetingParticipantsRequest
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.users.UserRepository

public class MeetingParticipantsApi(private val api: MeetacyApi) {
    public suspend fun list(
        token: Token,
        meetingId: MeetingId,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<UserRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = ListMeetingParticipantsRequest(
                token = token,
                meetingId = meetingId,
                amount = currentAmount,
                pagingId = currentPagingId
            )
        ).paging.mapItems { user -> UserRepository.of(user, api) }
    }

    public suspend fun paging(
        token: Token,
        meetingId: MeetingId,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<UserRepository> {
        return PagingSource(chunkSize, startPagingId, limit) { currentAmount, currentPagingId ->
            list(token, meetingId, currentAmount, currentPagingId).response
        }
    }
}
