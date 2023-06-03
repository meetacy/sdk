package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.Relationship
import app.meetacy.sdk.types.user.UserId

public class RegularUserRepository(
    override val data: RegularUser,
    private val api: MeetacyApi
) : UserRepository {
    public val id: UserId get() = data.id
    public val nickname: String get() = data.nickname
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    public val isFriend: Relationship get() = data.relationship

    public suspend fun addFriend(token: Token) {
        api.friends.add(token, data.id)
    }

    public suspend fun deleteFriend(token: Token) {
        api.friends.delete(token, data.id)
    }
}
