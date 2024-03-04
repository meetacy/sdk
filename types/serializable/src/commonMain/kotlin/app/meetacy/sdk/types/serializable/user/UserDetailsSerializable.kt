package app.meetacy.sdk.types.serializable.user

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.serializable.amount.AmountSerializable
import app.meetacy.sdk.types.serializable.amount.serializable
import app.meetacy.sdk.types.serializable.amount.type
import app.meetacy.sdk.types.serializable.email.EmailSerializable
import app.meetacy.sdk.types.serializable.email.type
import app.meetacy.sdk.types.serializable.file.FileIdSerializable
import app.meetacy.sdk.types.serializable.file.serializable
import app.meetacy.sdk.types.serializable.file.type
import app.meetacy.sdk.types.user.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UserDetailsSerializable(
    @SerialName("id")
    public val id: UserIdSerializable,
    @SerialName("nickname")
    public val nickname: String,
    @SerialName("subscribersAmount")
    public val subscribersAmount: AmountSerializable.OrZero,
    @SerialName("subscriptionsAmount")
    public val subscriptionsAmount: AmountSerializable.OrZero,
    @SerialName("avatarId")
    public val avatarId: FileIdSerializable? = null,
    @SerialName("isSelf")
    public val isSelf: Boolean,
    @SerialName("username")
    public val username: UsernameSerializable? = null,
    @SerialName("relationship")
    public val relationship: RelationshipSerializable? = null,
    @SerialName("email")
    public val email: EmailSerializable? = null,
    @SerialName("emailVerified")
    public val emailVerified: Boolean? = null,
)

public fun UserDetailsSerializable.type(): UserDetails = if (isSelf) {
    SelfUserDetails(
        id = id.type(),
        email = email?.type(),
        nickname = nickname,
        emailVerified = emailVerified ?: error("Self user must always return emailVerified parameter"),
        username = username?.type(),
        avatarId = avatarId?.type(),
        subscribersAmount = subscribersAmount.type(),
        subscriptionsAmount = subscriptionsAmount.type()
    )
} else {
    RegularUserDetails(
        id = id.type(),
        nickname = nickname,
        username = username?.type(),
        avatarId = avatarId?.type(),
        relationship = relationship?.type() ?: error("Regular user should always return relationship parameter"),
        subscribersAmount = subscribersAmount.type(),
        subscriptionsAmount = subscriptionsAmount.type()
    )
}

public fun UserDetails.serializable(): UserDetailsSerializable = UserDetailsSerializable(
    isSelf = isSelf,
    relationship = relationship?.serializable(),
    id = id.serializable(),
    nickname = nickname,
    username = username?.serializable(),
    avatarId = avatarId?.serializable(),
    subscribersAmount = subscribersAmount.serializable(),
    subscriptionsAmount = subscriptionsAmount.serializable()
)
