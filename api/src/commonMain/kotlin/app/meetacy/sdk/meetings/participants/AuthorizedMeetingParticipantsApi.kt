package app.meetacy.sdk.meetings.participants

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.paging.*
import app.meetacy.sdk.users.AuthorizedUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class AuthorizedMeetingParticipantsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: MeetingParticipantsApi get() = api.base.meetings.participants

    public suspend fun list(
        meetingId: MeetingId,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedUserRepository> = base
        .list(token, meetingId, amount, pagingId)
        .mapItems { user ->
            AuthorizedUserRepository.of(
                data = user.data,
                api = api
            )
        }

    public suspend fun paging(
        meetingId: MeetingId,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<AuthorizedUserRepository> = base
        .paging(token, meetingId, chunkSize, startPagingId, limit)
        .mapItems { user ->
            AuthorizedUserRepository.of(
                data = user.data,
                api = api
            )
        }
}
