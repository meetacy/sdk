package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId

public data class RegularUserDetails(
    override val relationship: Relationship,
    override val id: UserId,
    override val nickname: String,
    override val username: Username?,
    override val avatarId: FileId?,
    override val subscribersAmount: Amount.OrZero,
    override val subscriptionsAmount: Amount.OrZero
) : UserDetails {
    override val isSelf: Boolean get() = false
    override val email: Email? get() = null
    override val emailVerified: Nothing? get() = null

    override fun toUser(): RegularUser = RegularUser(
        id = id,
        nickname = nickname,
        username = username,
        avatarId = avatarId,
        relationship = relationship
    )
}
