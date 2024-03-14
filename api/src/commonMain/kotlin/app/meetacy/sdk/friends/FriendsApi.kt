package app.meetacy.sdk.friends

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.friends.location.FriendsLocationApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.RegularUserRepository
import app.meetacy.sdk.users.UserDetailsRepository

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.RegularUserRepository]
 */
public class FriendsApi(private val api: MeetacyApi) {
    public val location: FriendsLocationApi = FriendsLocationApi(api)

    public suspend fun add(token: Token, friendId: UserId) {
        api.engine.execute(AddFriendRequest(token, friendId))
    }
    public suspend fun delete(token: Token, friendId: UserId) {
        api.engine.execute(DeleteFriendRequest(token, friendId))
    }

    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<RegularUserRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = ListFriendsRequest(
                token = token,
                amount = currentAmount,
                pagingId = currentPagingId
            )
        ).paging.mapItems { regularUser ->
            RegularUserRepository(regularUser, api)
        }
    }

    public suspend fun subscriptions(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null,
        userId: UserId? = null
    ): PagingRepository<UserDetailsRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = GetSubscriptionsRequest(
                token = token,
                userId = userId,
                amount = currentAmount,
                pagingId = currentPagingId
            )
        ).paging.mapItems { userDetails ->
            UserDetailsRepository.of(userDetails, api)
        }
    }

    public suspend fun subscribers(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null,
        userId: UserId? = null,
    ): PagingRepository<UserDetailsRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = GetSubscribersRequest(
                token = token,
                userId = userId,
                amount = currentAmount,
                pagingId = currentPagingId
            )
        ).paging.mapItems { userDetails ->
            UserDetailsRepository.of(userDetails, api)
        }
    }

    public fun listPaging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<RegularUserRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            list(token, currentAmount, currentPagingId).response
        }
    }
    public fun subscribersPaging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<UserDetailsRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            subscribers(token, currentAmount, currentPagingId).response
        }
    }
    public fun subscriptionsPaging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<UserDetailsRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            subscriptions(token, currentAmount, currentPagingId).response
        }
    }
}
