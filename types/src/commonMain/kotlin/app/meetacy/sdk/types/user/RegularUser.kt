package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.file.FileId

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.users.RegularUserRepository]
 */
public data class RegularUser(
    override val id: UserId,
    override val nickname: String,
    override val username: Username?,
    override val avatarId: FileId?
) : User {
    override val isSelf: Boolean = false
}
