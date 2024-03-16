package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.Relationship
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username
import app.meetacy.sdk.users.subscribers.AuthorizedSubscribersRepository
import app.meetacy.sdk.users.subscriptions.AuthorizedSubscriptionsRepository

public class AuthorizedRegularUserRepository(
    override val data: RegularUser,
    private val api: AuthorizedMeetacyApi
) : AuthorizedUserRepository {
    override val base: RegularUserRepository get() = RegularUserRepository(data, api.base)

    override val id: UserId get() = data.id
    override val nickname: String get() = data.nickname
    override val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    override val username: Username? get() = data.username
    override val relationship: Relationship get() = data.relationship

    override val email: Nothing? get() = null
    override val emailVerified: Nothing? get() = null

    override val subscribers: AuthorizedSubscribersRepository = AuthorizedSubscribersRepository(api, id)
    override val subscriptions: AuthorizedSubscriptionsRepository = AuthorizedSubscriptionsRepository(api, id)

    public suspend fun addFriend() {
        api.friends.add(data.id)
    }

    public suspend fun deleteFriend() {
        api.friends.delete(data.id)
    }

    override suspend fun details(): AuthorizedUserDetailsRepository {
        return api.users.get(id)
    }
}
