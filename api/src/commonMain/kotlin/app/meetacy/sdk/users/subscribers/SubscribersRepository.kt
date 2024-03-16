package app.meetacy.sdk.users.subscribers

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.UserRepository

public class SubscribersRepository(
    private val api: MeetacyApi,
    private val userId: UserId
) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null,
    ): PagingRepository<UserRepository> {
        return api.friends.subscribers.list(token, amount, pagingId, userId)
    }

    public fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<UserRepository> {
        return api.friends.subscribers.paging(token, chunkSize, startPagingId, limit, userId)
    }
}
