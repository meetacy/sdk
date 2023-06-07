package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.auth.AuthorizedAuthApi
import app.meetacy.sdk.files.AuthorizedFilesApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.friends.AuthorizedFriendsApi
import app.meetacy.sdk.invitations.AuthorizedInvitationsApi
import app.meetacy.sdk.meetings.AuthorizedMeetingsApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username

public class SelfUserRepository(
    override val data: SelfUser,
    public val api: AuthorizedMeetacyApi
) : UserRepository {
    public val id: UserId get() = data.id
    public val email: Email? get() = data.email
    public val nickname: String get() = data.nickname
    public val emailVerified: Boolean get() = data.emailVerified
    public val username: Username? get() = data.username
    public val avatar: FileRepository? get() = FileRepository(data.avatarId, api)

    public val token: Token get() = api.token
    public val files: AuthorizedFilesApi get() = api.files
    public val auth: AuthorizedAuthApi get() = api.auth
    public val friends: AuthorizedFriendsApi get() = api.friends
    public val users: AuthorizedUsersApi get() = api.users
    public val meetings: AuthorizedMeetingsApi  get() = api.meetings
    public val invitations: AuthorizedInvitationsApi get() = api.invitations

    public suspend fun edited(
        nickname: String,
        username: Username?,
        avatarId: FileId?
    ): SelfUserRepository = api.base.users.edit(token, nickname, username, avatarId)

    public suspend fun edited(
        nickname: Optional<String> = Optional.Undefined,
        username: Optional<Username?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): SelfUserRepository = api.base.users.edit(token, nickname, username, avatarId)
}
