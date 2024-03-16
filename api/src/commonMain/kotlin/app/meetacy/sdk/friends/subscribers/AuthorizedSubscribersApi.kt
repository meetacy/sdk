package app.meetacy.sdk.friends.subscribers

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.paging.mapItems
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.AuthorizedUserRepository

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.subscribers.AuthorizedSubscribersRepository]
 */
public class AuthorizedSubscribersApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: SubscribersApi get() = api.base.friends.subscribers

    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null,
        userId: UserId? = null
    ): PagingRepository<AuthorizedUserRepository> {
        return base.list(token, amount, pagingId, userId).mapItems { user ->
            AuthorizedUserRepository.of(user.data, api)
        }
    }

    public fun paging(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null,
        userId: UserId? = null
    ): PagingSource<AuthorizedUserRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            list(currentAmount, currentPagingId, userId).response
        }
    }
}
