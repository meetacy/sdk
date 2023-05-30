package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.file.FileId

public sealed interface User {
    public val id: UserId
    public val nickname: String
    public val avatarId: FileId?
    public val isSelf: Boolean
    public val isFriend: Boolean?
}
