package app.meetacy.sdk.friends

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.AddFriendRequest
import app.meetacy.sdk.engine.requests.DeleteFriendRequest
import app.meetacy.sdk.engine.requests.ListFriendsRequest
import app.meetacy.sdk.internal.paging.pagingFlow
import app.meetacy.sdk.users.RegularUserRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.paging.mapItems
import app.meetacy.sdk.types.user.UserId
import kotlinx.coroutines.flow.Flow

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.RegularUserRepository]
 */
public class FriendsApi(private val api: MeetacyApi) {
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
    ): PagingResponse<List<RegularUserRepository>> {
        return api.engine.execute(
            request = ListFriendsRequest(
                token = token,
                amount = amount,
                pagingId = pagingId
            )
        ).paging.mapItems { regularUser ->
            RegularUserRepository(regularUser, api)
        }
    }

    public fun flow(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<RegularUserRepository>> {
        return pagingFlow(chunkSize, startPagingId, limit) { pagingId, amount ->
            list(token, amount, pagingId)
        }
    }
}
