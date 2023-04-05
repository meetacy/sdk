package app.meetacy.sdk.friends

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.UserId
import kotlinx.coroutines.flow.Flow

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.AuthorizedRegularUserRepository]
 */
public class AuthorizedFriendsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: FriendsApi get() = api.base.friends

    public suspend fun add(friendId: UserId) {
        base.add(token, friendId)
    }
    public suspend fun delete(friendId: UserId) {
        base.delete(token, friendId)
    }
    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<RegularUser>> = base.list(token, amount, pagingId)

    public fun flow(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): Flow<List<RegularUser>> = base.flow(token, chunkSize, startPagingId, limit)
}
