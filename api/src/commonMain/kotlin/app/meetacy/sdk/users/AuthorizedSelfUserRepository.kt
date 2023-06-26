package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.auth.AuthorizedAuthApi
import app.meetacy.sdk.files.AuthorizedFilesApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.friends.AuthorizedFriendsApi
import app.meetacy.sdk.invitations.AuthorizedInvitationsApi
import app.meetacy.sdk.meetings.AuthorizedMeetingsApi
import app.meetacy.sdk.notifications.AuthorizedNotificationsApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username
import app.meetacy.sdk.updates.AuthorizedUpdatesApi

public class AuthorizedSelfUserRepository(
    override val data: SelfUser,
    public val api: AuthorizedMeetacyApi
) : AuthorizedUserRepository {
    override val base: SelfUserRepository get() = SelfUserRepository(data, api.base)

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
    public val meetings: AuthorizedMeetingsApi get() = api.meetings
    public val invitations: AuthorizedInvitationsApi get() = api.invitations
    public val notifications: AuthorizedNotificationsApi get() = api.notifications
    public val updates: AuthorizedUpdatesApi get() = api.updates

    public suspend fun edited(
        nickname: String,
        username: Username?,
        avatarId: FileId?
    ): AuthorizedSelfUserRepository = api.users.edit(nickname, username, avatarId)

    public suspend fun edited(
        nickname: Optional<String> = Optional.Undefined,
        username: Optional<Username?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): AuthorizedSelfUserRepository = api.users.edit(nickname, username, avatarId)

    public suspend fun updated(): AuthorizedSelfUserRepository = api.getMe()
}
