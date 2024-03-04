package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.auth.AuthApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.files.FilesApi
import app.meetacy.sdk.friends.FriendsApi
import app.meetacy.sdk.invitations.InvitationsApi
import app.meetacy.sdk.meetings.MeetingsApi
import app.meetacy.sdk.notifications.NotificationsApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUserDetails
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username
import app.meetacy.sdk.updates.UpdatesApi

public class SelfUserDetailsRepository(
    override val data: SelfUserDetails,
    public val api: MeetacyApi
) : UserDetailsRepository {
    override val id: UserId get() = data.id
    override val email: Email? get() = data.email
    override val nickname: String get() = data.nickname
    override val emailVerified: Boolean get() = data.emailVerified
    override val username: Username? get() = data.username
    override val avatar: FileRepository? get() = FileRepository(data.avatarId, api)
    override val subscribersAmount: Amount.OrZero get() = data.subscribersAmount
    override val subscriptionsAmount: Amount.OrZero get() = data.subscriptionsAmount

    override val isSelf: Boolean get() = true
    override val relationship: Nothing? get() = null

    public val files: FilesApi get() = api.files
    public val auth: AuthApi get() = api.auth
    public val friends: FriendsApi get() = api.friends
    public val users: UsersApi get() = api.users
    public val meetings: MeetingsApi get() = api.meetings
    public val invitations: InvitationsApi get() = api.invitations
    public val notifications: NotificationsApi get() = api.notifications
    public val updates: UpdatesApi get() = api.updates

    public suspend fun edited(
        token: Token,
        nickname: String,
        username: Username?,
        avatarId: FileId?
    ): SelfUserRepository = api.users.edit(token, nickname, username, avatarId)

    public suspend fun edited(
        token: Token,
        nickname: Optional<String> = Optional.Undefined,
        username: Optional<Username?> = Optional.Undefined,
        avatarId: Optional<FileId?> = Optional.Undefined
    ): SelfUserRepository = api.users.edit(token, nickname, username, avatarId)

    public suspend fun usernameAvailable(username: Username): Username {
        return api.users.usernameAvailable(username)
    }

    override suspend fun updated(token: Token): UserDetailsRepository {
        return api.getMe(token)
    }

    override fun toUser(): SelfUserRepository {
        return SelfUserRepository(data.toUser(), api)
    }
}
