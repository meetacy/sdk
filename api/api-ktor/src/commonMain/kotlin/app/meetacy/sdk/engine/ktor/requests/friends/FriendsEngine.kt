@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.engine.ktor.requests.friends

import app.meetacy.sdk.engine.requests.AddFriendRequest
import app.meetacy.sdk.engine.requests.DeleteFriendRequest
import app.meetacy.sdk.engine.requests.ListFriendsRequest
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.UserId
import dev.icerock.moko.network.generated.apis.FriendsApi
import dev.icerock.moko.network.generated.apis.FriendsApiImpl
import dev.icerock.moko.network.generated.models.AccessFriendRequest
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.ListFriendsRequest as GeneratedListFriendsRequest

internal class FriendsEngine(
    baseUrl: String,
    httpClient: HttpClient,
    json: Json
) {
    private val base: FriendsApi = FriendsApiImpl(baseUrl, httpClient, json)

    suspend fun add(request: AddFriendRequest) {
        base.friendsAddPost(
            accessFriendRequest = AccessFriendRequest(
                accessIdentity = request.token.string,
                friendIdentity = request.friendId.string
            )
        )
    }

    suspend fun delete(request: DeleteFriendRequest) {
        base.friendsDeletePost(
            accessFriendRequest = AccessFriendRequest(
                accessIdentity = request.token.string,
                friendIdentity = request.friendId.string
            )
        )
    }

    suspend fun list(request: ListFriendsRequest): ListFriendsRequest.Response {
        val response = base.friendsListPost(
            listFriendsRequest = GeneratedListFriendsRequest(
                accessIdentity = request.token.string,
                amount = request.amount.int,
                pagingId = request.pagingId?.string
            )
        )

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map { user ->
                RegularUser(
                    id = UserId(user.identity),
                    nickname = user.nickname,
                    avatarId = user.avatarIdentity?.let(::FileId)
                )
            }
        )

        return ListFriendsRequest.Response(paging)
    }
}