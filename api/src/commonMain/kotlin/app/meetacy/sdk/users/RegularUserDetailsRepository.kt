package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.*
import app.meetacy.sdk.users.subscribers.SubscribersRepository
import app.meetacy.sdk.users.subscriptions.SubscriptionsRepository

public class RegularUserDetailsRepository(
    override val data: RegularUserDetails,
    private val api: MeetacyApi
) : UserDetailsRepository {
    override val id: UserId get() = data.id
    override val nickname: String get() = data.nickname
    override val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    override val relationship: Relationship get() = data.relationship
    override val username: Username? get() = data.username
    override val subscribersAmount: Amount.OrZero get() = data.subscribersAmount
    override val subscriptionsAmount: Amount.OrZero get() = data.subscriptionsAmount

    override val isSelf: Boolean get() = false
    override val email: Nothing? get() = null
    override val emailVerified: Nothing? get() = null

    override val subscribers: SubscribersRepository = SubscribersRepository(api, id)
    override val subscriptions: SubscriptionsRepository = SubscriptionsRepository(api, id)

    public suspend fun addFriend(token: Token) {
        api.friends.add(token, data.id)
    }

    public suspend fun deleteFriend(token: Token) {
        api.friends.delete(token, data.id)
    }

    public suspend fun usernameAvailable(username: Username): Username {
        return api.users.usernameAvailable(username)
    }

    override suspend fun updated(token: Token): RegularUserDetailsRepository {
        // Cast is fine since we already know that user associated with that id is regular
        return api.users.get(token, id) as RegularUserDetailsRepository
    }

    override fun toUser(): RegularUserRepository {
        return RegularUserRepository(data.toUser(), api)
    }
}
