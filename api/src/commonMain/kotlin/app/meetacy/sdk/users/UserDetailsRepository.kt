package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.user.*
import app.meetacy.sdk.users.subscribers.SubscribersRepository
import app.meetacy.sdk.users.subscriptions.SubscriptionsRepository

public sealed interface UserDetailsRepository {
    public val data: UserDetails

    public val isSelf: Boolean
    public val relationship: Relationship?
    public val id: UserId
    public val nickname: String
    public val username: Username?
    public val email: Email?
    public val emailVerified: Boolean?
    public val avatar: FileRepository?
    public val subscribersAmount: Amount.OrZero
    public val subscriptionsAmount: Amount.OrZero

    public val subscribers: SubscribersRepository
    public val subscriptions: SubscriptionsRepository

    public suspend fun updated(token: Token): UserDetailsRepository
    public fun toUser(): UserRepository

    public companion object {
        public fun of(
            data: UserDetails,
            api: MeetacyApi
        ): UserDetailsRepository = when (data) {
            is RegularUserDetails -> RegularUserDetailsRepository(data, api)
            is SelfUserDetails -> SelfUserDetailsRepository(data, api)
        }
    }
}
