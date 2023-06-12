package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.Relationship
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username

public class AuthorizedRegularUserRepository(
    override val data: RegularUser,
    private val api: AuthorizedMeetacyApi
) : AuthorizedUserRepository {
    override val base: RegularUserRepository get() = RegularUserRepository(data, api.base)

    public val id: UserId get() = data.id
    public val nickname: String get() = data.nickname
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    public val username: Username? get() = data.username
    public val isFriend: Relationship get() = data.relationship

    public suspend fun addFriend() {
        api.friends.add(data.id)
    }

    public suspend fun deleteFriend() {
        api.friends.delete(data.id)
    }
}
