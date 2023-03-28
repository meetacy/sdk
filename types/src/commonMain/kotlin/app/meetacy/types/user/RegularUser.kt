package app.meetacy.types.user

import app.meetacy.types.file.FileId

public data class RegularUser(
    override val id: UserId,
    override val nickname: String,
    override val avatarId: FileId?
) : User
