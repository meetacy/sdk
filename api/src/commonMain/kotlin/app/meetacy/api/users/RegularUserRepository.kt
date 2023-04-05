package app.meetacy.api.users

import app.meetacy.api.MeetacyApi
import app.meetacy.api.files.FileRepository
import app.meetacy.types.auth.Token
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.UserId

public class RegularUserRepository(
    public val data: RegularUser,
    private val api: MeetacyApi
) : UserRepository {
    public val id: UserId get() = data.id
    public val nickname: String get() = data.nickname
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)

    public suspend fun addFriend(token: Token) {
        api.friends.add(token, data.id)
    }
}
