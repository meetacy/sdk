package app.meetacy.sdk.friends

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.AddFriendRequest
import app.meetacy.sdk.engine.requests.DeleteFriendRequest
import app.meetacy.sdk.engine.requests.ListFriendsRequest
import app.meetacy.sdk.internal.paging.pagingFlow
import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.UserId
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
    ): PagingResponse<List<RegularUser>> = api.engine.execute(
        request = ListFriendsRequest(
            token = token,
            amount = amount,
            pagingId = pagingId
        )
    ).paging

    public fun flow(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<RegularUser>> = pagingFlow(chunkSize, startPagingId, limit) { pagingId, amount ->
        list(token, amount, pagingId)
    }
}
