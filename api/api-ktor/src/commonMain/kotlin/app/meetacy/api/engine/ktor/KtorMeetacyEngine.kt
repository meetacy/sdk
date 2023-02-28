@file:OptIn(UnsafeConstructor::class)

package app.meetacy.api.engine.ktor

import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.AddFriendRequest
import app.meetacy.api.engine.requests.ConfirmEmailRequest
import app.meetacy.api.engine.requests.DeleteFriendRequest
import app.meetacy.api.engine.requests.GenerateAuthRequest
import app.meetacy.api.engine.requests.GetMeRequest
import app.meetacy.api.engine.requests.GetUserRequest
import app.meetacy.api.engine.requests.LinkEmailRequest
import app.meetacy.api.engine.requests.ListFriendsRequest
import app.meetacy.api.engine.requests.MeetacyRequest
import app.meetacy.api.engine.updates.MeetacyUpdate
import app.meetacy.api.engine.updates.filter.MeetacyUpdateFilter
import app.meetacy.types.annotation.UnsafeConstructor
import app.meetacy.types.auth.Token
import app.meetacy.types.email.ConfirmEmailStatus
import app.meetacy.types.email.Email
import app.meetacy.types.paging.PagingResponse
import app.meetacy.types.update.UpdateId
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.SelfUser
import app.meetacy.types.user.UserId
import dev.icerock.moko.network.generated.apis.AuthApi
import dev.icerock.moko.network.generated.apis.AuthApiImpl
import dev.icerock.moko.network.generated.apis.FriendsApi
import dev.icerock.moko.network.generated.apis.FriendsApiImpl
import dev.icerock.moko.network.generated.apis.UserApi
import dev.icerock.moko.network.generated.apis.UserApiImpl
import dev.icerock.moko.network.generated.models.AccessFriendRequest
import dev.icerock.moko.network.generated.models.AccessIdentityRequest
import dev.icerock.moko.network.generated.models.EmailConfirmRequest
import dev.icerock.moko.network.generated.models.EmailLinkRequest
import dev.icerock.moko.network.generated.models.GenerateIdentityRequest
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.GetUserRequest as GetUserRequestGenerated

public class KtorMeetacyEngine(
    baseUrl: String,
    httpClient: HttpClient = HttpClient(),
    json: Json = Json,
) : MeetacyRequestsEngine {

    private val authApi: AuthApi = AuthApiImpl(
        basePath = baseUrl,
        httpClient = httpClient,
        json = json
    )

    private val userApi: UserApi = UserApiImpl(
        basePath = baseUrl,
        httpClient = httpClient,
        json = json
    )

    private val friendsApi: FriendsApi = FriendsApiImpl(
        basePath = baseUrl,
        httpClient = httpClient,
        json = json
    )

    override fun updatesPolling(
        token: Token,
        vararg filters: MeetacyUpdateFilter<*>,
        lastUpdateId: UpdateId?,
    ): Flow<MeetacyUpdate> = emptyFlow() // TODO: create updates from socket

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> execute(request: MeetacyRequest<T>): T =
        when (request) {
            is AddFriendRequest -> request.execute() as T
            is ConfirmEmailRequest -> request.execute() as T
            is DeleteFriendRequest -> request.execute() as T
            is GenerateAuthRequest -> request.execute() as T
            is GetMeRequest -> request.execute() as T
            is GetUserRequest -> request.execute() as T
            is LinkEmailRequest -> request.execute() as T
            is ListFriendsRequest -> request.execute() as T
        }

    private suspend fun AddFriendRequest.execute() {
        friendsApi.friendsAddPost(
            AccessFriendRequest(
                accessIdentity = token.string,
                friendIdentity = friendId.string,
            )
        )
    }

    // FIXME need rework
    private suspend fun ConfirmEmailRequest.execute(): ConfirmEmailRequest.Response {
        return authApi.authEmailConfirmPost(
            EmailConfirmRequest(
                email = email.string,
                confirmHash = confirmHash.string
            )
        ).let { ConfirmEmailRequest.Response(ConfirmEmailStatus.Success) }
    }

    private suspend fun DeleteFriendRequest.execute() {
        friendsApi.friendsDeletePost(
            AccessFriendRequest(
                accessIdentity = token.string,
                friendIdentity = friendId.string
            )
        )
    }


    private suspend fun GenerateAuthRequest.execute(): GenerateAuthRequest.Response {
        return authApi.authGeneratePost(
            GenerateIdentityRequest(
                nickname = nickname
            )
        ).let { GenerateAuthRequest.Response(Token(it.result)) }
    }

    private suspend fun GetMeRequest.execute(): GetMeRequest.Response {
        return userApi.usersGetPost(
            GetUserRequestGenerated(identity = token.string)
        ).result?.let { result ->
            GetMeRequest.Response(
                SelfUser(
                    id = UserId(result.identity),
                    email = result.email?.let(::Email),
                    emailVerified = result.emailVerified ?: false,
                    nickname = result.nickname
                )
            )
        } ?: error("response not contains result")
    }

    private suspend fun GetUserRequest.execute(): GetUserRequest.Response {
        return userApi.usersGetPost(
            GetUserRequestGenerated(identity = token.string, accessIdentity = userId.string)
        ).result?.let { result ->
            GetUserRequest.Response(
                RegularUser(
                    id = UserId(result.identity),
                    email = result.email?.let(::Email),
                    nickname = result.nickname
                )
            )
        } ?: error("response not contains result")
    }

    private suspend fun LinkEmailRequest.execute() {
        authApi.authEmailLinkPost(
            EmailLinkRequest(
                email = email.string,
                accessIdentity = token.string
            )
        )
    }

    private suspend fun ListFriendsRequest.execute(): ListFriendsRequest.Response {
        return friendsApi.friendsListPost(
            AccessIdentityRequest(
                accessIdentity = token.string,
                // FIXME add pagination params
                /** amount = , **/
                /** pagingId = **/
            )
        ).result.let { friendsAndSubscriptions ->
            // FIXME rework union friend and subscriptions logic
            val users = (friendsAndSubscriptions.friends + friendsAndSubscriptions.subscriptions)
                .map { friend ->
                    RegularUser(
                        id = UserId(friend.identity),
                        nickname = friend.nickname
                    )
                }
            ListFriendsRequest.Response(PagingResponse(nextPagingId = null, users) )
        }
    }
}