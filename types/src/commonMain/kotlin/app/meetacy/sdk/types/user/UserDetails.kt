package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId

public sealed interface UserDetails {
    public val isSelf: Boolean
    public val relationship: Relationship?
    public val id: UserId
    public val nickname: String
    public val username: Username?
    public val email: Email?
    public val emailVerified: Boolean?
    public val avatarId: FileId?
    public val subscribersAmount: Amount.OrZero
    public val subscriptionsAmount: Amount.OrZero

    public fun toUser(): User
}
