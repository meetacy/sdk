package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.auth.AuthApi
import app.meetacy.sdk.auth.AuthorizedAuthApi
import app.meetacy.sdk.files.AuthorizedFilesApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.files.FilesApi
import app.meetacy.sdk.friends.AuthorizedFriendsApi
import app.meetacy.sdk.friends.FriendsApi
import app.meetacy.sdk.invitations.AuthorizedInvitationsApi
import app.meetacy.sdk.invitations.InvitationsApi
import app.meetacy.sdk.meetings.AuthorizedMeetingsApi
import app.meetacy.sdk.meetings.MeetingsApi
import app.meetacy.sdk.notifications.AuthorizedNotificationsApi
import app.meetacy.sdk.notifications.NotificationsApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUserDetails
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.types.user.Username
import app.meetacy.sdk.updates.AuthorizedUpdatesApi
import app.meetacy.sdk.updates.UpdatesApi
import app.meetacy.sdk.users.subscribers.AuthorizedSubscribersRepository
import app.meetacy.sdk.users.subscriptions.AuthorizedSubscriptionsRepository

public class AuthorizedSelfUserDetailsRepository(
    override val data: SelfUserDetails,
    public val api: AuthorizedMeetacyApi
) : AuthorizedUserDetailsRepository {
    override val base: SelfUserDetailsRepository get() = SelfUserDetailsRepository(data, api.base)

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

    override val subscribers: AuthorizedSubscribersRepository = AuthorizedSubscribersRepository(api, id)
    override val subscriptions: AuthorizedSubscriptionsRepository = AuthorizedSubscriptionsRepository(api, id)

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

    public suspend fun usernameAvailable(username: Username): Username {
        return api.users.usernameAvailable(username)
    }

    override suspend fun updated(): AuthorizedSelfUserDetailsRepository {
        return api.getMe()
    }

    override fun toUser(): AuthorizedSelfUserRepository {
        return AuthorizedSelfUserRepository(data.toUser(), api)
    }
}
