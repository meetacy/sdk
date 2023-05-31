package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.file.FileId

public sealed interface User {
    public val id: UserId
    public val nickname: String
    public val avatarId: FileId?
    public val isSelf: Boolean
    public val isFriend: Relationship?
}

public sealed interface Relationship {
    public data object None: Relationship
    public data object Subscription: Relationship
    public data object Subscriber: Relationship
    public data object Friend: Relationship
}