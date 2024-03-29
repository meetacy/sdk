package app.meetacy.sdk.friends

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.friends.location.AuthorizedFriendsLocationApi
import app.meetacy.sdk.friends.subscribers.AuthorizedSubscribersApi
import app.meetacy.sdk.friends.subscriptions.AuthorizedSubscriptionsApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.paging.mapItems
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.AuthorizedRegularUserRepository

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.AuthorizedRegularUserRepository]
 */
public class AuthorizedFriendsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: FriendsApi get() = api.base.friends

    public val location: AuthorizedFriendsLocationApi = AuthorizedFriendsLocationApi(api)
    public val subscribers: AuthorizedSubscribersApi = AuthorizedSubscribersApi(api)
    public val subscriptions: AuthorizedSubscriptionsApi = AuthorizedSubscriptionsApi(api)

    public suspend fun add(friendId: UserId) {
        base.add(token, friendId)
    }
    public suspend fun delete(friendId: UserId) {
        base.delete(token, friendId)
    }

    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedRegularUserRepository> =
        base.list(token, amount, pagingId).mapItems { user ->
            AuthorizedRegularUserRepository(
                data = user.data,
                api = api
            )
        }

    public fun paging(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<AuthorizedRegularUserRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            list(currentAmount, currentPagingId).response
        }
    }
}
