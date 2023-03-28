package app.meetacy.types.user

import app.meetacy.types.file.FileId

public interface User {
    public val id: UserId
    public val nickname: String
    public val avatarId: FileId?
}
