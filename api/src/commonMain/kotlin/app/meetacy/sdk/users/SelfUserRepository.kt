package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.auth.AuthorizedAuthApi
import app.meetacy.sdk.files.AuthorizedFilesApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.friends.AuthorizedFriendsApi
import app.meetacy.sdk.meetings.AuthorizedMeetingsApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.UserId

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

    public suspend fun edited(
        nickname: String,
        avatarId: FileId?
    ): SelfUser = api.users.edit(nickname, avatarId)

    public suspend fun edited(
        nickname: Optional<String> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): SelfUser = api.users.edit(nickname, avatarId)
}
