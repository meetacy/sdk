package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.Relationship
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username

public class RegularUserRepository(
    override val data: RegularUser,
    private val api: MeetacyApi
) : UserRepository {
    override val id: UserId get() = data.id
    override val nickname: String get() = data.nickname
    override val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    override val relationship: Relationship get() = data.relationship
    override val username: Username? get() = data.username

    override val isSelf: Boolean get() = false
    override val email: Nothing? get() = null
    override val emailVerified: Nothing? get() = null

    public suspend fun addFriend(token: Token) {
        api.friends.add(token, data.id)
    }

    public suspend fun deleteFriend(token: Token) {
        api.friends.delete(token, data.id)
    }

    public suspend fun usernameAvailable(username: Username): Username {
        return api.users.usernameAvailable(username)
    }

    override suspend fun details(token: Token): UserDetailsRepository {
        return api.users.get(token, id)
    }
}
