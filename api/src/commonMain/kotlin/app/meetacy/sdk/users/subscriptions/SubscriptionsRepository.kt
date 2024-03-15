package app.meetacy.sdk.users.subscriptions

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.UserRepository

public class SubscriptionsRepository(
    private val api: MeetacyApi,
    private val userId: UserId
) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null,
    ): PagingRepository<UserRepository> {
        return api.friends.subscriptions.list(token, amount, pagingId, userId)
    }

    public fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<UserRepository> {
        return api.friends.subscriptions.paging(token, chunkSize, startPagingId, limit, userId)
    }
}
