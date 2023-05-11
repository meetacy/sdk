package app.meetacy.sdk.friends

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.friends.location.AuthorizedFriendsLocationApi
import app.meetacy.sdk.users.RegularUserRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.paging.mapItems
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.AuthorizedRegularUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.AuthorizedRegularUserRepository]
 */
public class AuthorizedFriendsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: FriendsApi get() = api.base.friends

    public val location: AuthorizedFriendsLocationApi = AuthorizedFriendsLocationApi(api)

    public suspend fun add(friendId: UserId) {
        base.add(token, friendId)
    }
    public suspend fun delete(friendId: UserId) {
        base.delete(token, friendId)
    }

    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingResponse<List<AuthorizedRegularUserRepository>> {
        return base.list(token, amount, pagingId).mapItems { user ->
            AuthorizedRegularUserRepository(
                data = user.data,
                api = api
            )
        }
    }

    public fun paging(
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingRepository<List<AuthorizedRegularUserRepository>> {
        return base.paging(token, chunkSize, startPagingId, limit).map { userList ->
            userList.map { user ->
                AuthorizedRegularUserRepository(
                    data = user.data,
                    api = api
                )
            }
        }
    }
}
