package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.user.*
import app.meetacy.sdk.users.subscribers.AuthorizedSubscribersRepository
import app.meetacy.sdk.users.subscriptions.AuthorizedSubscriptionsRepository

public sealed interface AuthorizedUserDetailsRepository {
    public val data: UserDetails
    public val base: UserDetailsRepository

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

    public val subscribers: AuthorizedSubscribersRepository
    public val subscriptions: AuthorizedSubscriptionsRepository

    public fun toUser(): AuthorizedUserRepository
    public suspend fun updated(): AuthorizedUserDetailsRepository

    public companion object {
        public fun of(
            data: UserDetails,
            api: AuthorizedMeetacyApi
        ): AuthorizedUserDetailsRepository = when (data) {
            is RegularUserDetails -> AuthorizedRegularUserDetailsRepository(data, api)
            is SelfUserDetails -> AuthorizedSelfUserDetailsRepository(data, api)
        }
    }
}
