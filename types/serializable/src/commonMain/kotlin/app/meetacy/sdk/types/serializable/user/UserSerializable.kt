package app.meetacy.sdk.types.serializable.user

import app.meetacy.sdk.types.serializable.email.EmailSerializable
import app.meetacy.sdk.types.serializable.email.type
import app.meetacy.sdk.types.serializable.file.FileIdSerializable
import app.meetacy.sdk.types.serializable.file.type
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UserSerializable(
    @SerialName("id")
    public val id: UserIdSerializable,
    @SerialName("nickname")
    public val nickname: String,
    @SerialName("avatar_id")
    public val avatarId: FileIdSerializable?,
    @SerialName("is_self")
    public val isSelf: Boolean,
    @SerialName("username")
    public val username: UsernameSerializable?,
    @SerialName("relationship")
    public val relationship: RelationshipSerializable? = null,
    @SerialName("email")
    public val email: EmailSerializable? = null,
    @SerialName("email_verified")
    public val emailVerified: Boolean? = null
)

public fun UserSerializable.type(): User = if (isSelf) {
    SelfUser(
        id = id.type(),
        email = email?.type(),
        nickname = nickname,
        emailVerified = emailVerified!!,
        username = username?.type(),
        avatarId = avatarId?.type()
    )
} else {
    RegularUser(
        id = id.type(),
        nickname = nickname,
        username = username?.type(),
        avatarId = avatarId?.type(),
        relationship = relationship!!.type()
    )
}
