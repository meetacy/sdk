package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.*

public class AuthorizedRegularUserDetailsRepository(
    override val data: RegularUserDetails,
    private val api: AuthorizedMeetacyApi
) : AuthorizedUserDetailsRepository {
    override val base: RegularUserDetailsRepository get() = RegularUserDetailsRepository(data, api.base)

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

    public suspend fun addFriend() {
        api.friends.add(data.id)
    }

    public suspend fun deleteFriend() {
        api.friends.delete(data.id)
    }

    public suspend fun usernameAvailable(username: Username): Username {
        return api.users.usernameAvailable(username)
    }

    override suspend fun updated(): AuthorizedRegularUserDetailsRepository {
        // Cast is fine since we already know that user associated with that id is regular
        return api.users.get(id) as AuthorizedRegularUserDetailsRepository
    }

    override fun toUser(): AuthorizedRegularUserRepository {
        return AuthorizedRegularUserRepository(data.toUser(), api)
    }
}
