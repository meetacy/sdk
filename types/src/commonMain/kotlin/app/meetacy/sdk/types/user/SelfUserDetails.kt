package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId

public data class SelfUserDetails(
    override val id: UserId,
    override val nickname: String,
    override val username: Username?,
    override val email: Email?,
    override val emailVerified: Boolean,
    override val avatarId: FileId?,
    override val subscribersAmount: Amount.OrZero,
    override val subscriptionsAmount: Amount.OrZero
) : UserDetails {
    override val isSelf: Boolean get() = true
    override val relationship: Nothing? get() = null

    override fun toUser(): SelfUser = SelfUser(
        id = id,
        nickname = nickname,
        username = username,
        email = email,
        emailVerified = emailVerified,
        avatarId = avatarId
    )
}
