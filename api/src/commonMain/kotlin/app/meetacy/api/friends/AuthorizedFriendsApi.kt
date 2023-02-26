package app.meetacy.api.friends

import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.friend.Friend
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.user.UserId
import kotlinx.coroutines.flow.Flow

public class AuthorizedFriendsApi(
    public val token: Token,
    public val base: FriendsApi
) {
    public suspend fun add(friendId: UserId) {
        base.add(token, friendId)
    }
    public suspend fun delete(friendId: UserId) {
        base.delete(token, friendId)
    }
    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<Friend>> = base.list(token, amount, pagingId)

    public fun flow(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<PagingResponse<List<Friend>>> = base.flow(token, chunkSize, startPagingId, limit)
}
