package app.meetacy.types.user

import app.meetacy.types.file.FileId

public sealed interface User {
    public val id: UserId
    public val nickname: String
    public val avatarId: FileId?
    public val isSelf: Boolean
}
