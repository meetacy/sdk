package app.meetacy.api.friends

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.AddFriendRequest
import app.meetacy.api.engine.requests.DeleteFriendRequest
import app.meetacy.api.engine.requests.ListFriendsRequest
import app.meetacy.api.internal.paging.pagingFlow
import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.UserId
import kotlinx.coroutines.flow.Flow

public class FriendsApi(private val engine: MeetacyRequestsEngine) {
    public suspend fun add(token: Token, friendId: UserId) {
        engine.execute(AddFriendRequest(token, friendId))
    }
    public suspend fun delete(token: Token, friendId: UserId) {
        engine.execute(DeleteFriendRequest(token, friendId))
    }

    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<RegularUser>> = engine.execute(
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
