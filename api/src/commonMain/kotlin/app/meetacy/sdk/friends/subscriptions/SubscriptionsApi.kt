package app.meetacy.sdk.friends.subscriptions

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListSubscriptionsRequest
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.UserRepository

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.friends.subscriptions.AuthorizedSubscriptionsApi]
 */
public class SubscriptionsApi(private val api: MeetacyApi) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null,
        userId: UserId? = null
    ): PagingRepository<UserRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = ListSubscriptionsRequest(
                token = token,
                userId = userId,
                amount = currentAmount,
                pagingId = currentPagingId
            )
        ).paging.mapItems { user -> UserRepository.of(user, api) }
    }

    public fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null,
        userId: UserId? = null
    ): PagingSource<UserRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            list(token, currentAmount, currentPagingId, userId).response
        }
    }
}
