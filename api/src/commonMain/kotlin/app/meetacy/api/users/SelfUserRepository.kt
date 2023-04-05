package app.meetacy.api.users

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.auth.AuthorizedAuthApi
import app.meetacy.api.files.AuthorizedFilesApi
import app.meetacy.api.files.FileRepository
import app.meetacy.api.friends.AuthorizedFriendsApi
import app.meetacy.api.meetings.AuthorizedMeetingsApi
import app.meetacy.types.auth.Token
import app.meetacy.types.email.Email
import app.meetacy.types.user.SelfUser
import app.meetacy.types.user.UserId

public class SelfUserRepository(
    public val data: SelfUser,
    public val api: AuthorizedMeetacyApi
) : UserRepository {
    public val id: UserId get() = data.id
    public val email: Email? get() = data.email
    public val nickname: String get() = data.nickname
    public val emailVerified: Boolean get() = data.emailVerified
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)

    public val token: Token get() = api.token
    public val files: AuthorizedFilesApi get() = api.files
    public val auth: AuthorizedAuthApi get() = api.auth
    public val friends: AuthorizedFriendsApi get() = api.friends
    public val users: AuthorizedUsersApi get() = api.users
    public val meetings: AuthorizedMeetingsApi  get() = api.meetings
}
