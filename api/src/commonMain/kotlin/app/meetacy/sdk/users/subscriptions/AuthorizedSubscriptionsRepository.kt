package app.meetacy.sdk.users.subscriptions

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.AuthorizedUserRepository

public class AuthorizedSubscriptionsRepository(
    private val api: AuthorizedMeetacyApi,
    private val userId: UserId
) {
    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null,
    ): PagingRepository<AuthorizedUserRepository> {
        return api.friends.subscriptions.list(amount, pagingId, userId)
    }

    public fun paging(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<AuthorizedUserRepository> {
        return api.friends.subscriptions.paging(chunkSize, startPagingId, limit, userId)
    }
}
