package app.meetacy.sdk.meetings.history

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.*

public class AuthorizedMeetingsHistoryApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: MeetingsHistoryApi get() = api.base.meetings.history

    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedMeetingRepository> = base
        .list(token, amount, pagingId)
        .mapItems { meeting ->
            AuthorizedMeetingRepository(
                data = meeting.data,
                api = api
            )
        }

    public suspend fun active(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedMeetingRepository> = base
        .active(token, amount, pagingId)
        .mapItems { meeting ->
            AuthorizedMeetingRepository(meeting.data, api)
        }

    public suspend fun past(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedMeetingRepository> = base
        .past(token, amount, pagingId)
        .mapItems { meeting ->
            AuthorizedMeetingRepository(meeting.data, api)
        }

    public fun paging(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<AuthorizedMeetingRepository> = base
        .paging(token, chunkSize, startPagingId, limit)
        .map { meetings ->
            meetings.map { meeting ->
                AuthorizedMeetingRepository(
                    data = meeting.data,
                    api = api
                )
            }
        }
}
