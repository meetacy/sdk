package app.meetacy.sdk.invitations

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.UserRepository
import kotlin.jvm.JvmName

public class AuthorizedInvitationsApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: InvitationsApi = api.base.invitations

    public suspend fun create(
        invitedUser: User,
        expiryDate: DateTime,
        meetingId: MeetingId
    ): AuthorizedInvitationRepository {
        val invitation = base.create(token, invitedUser, expiryDate, meetingId).data

        return AuthorizedInvitationRepository(invitation, api)
    }

    public suspend fun accept(invitationId: InvitationId) {
        base.accept(token, invitationId)
    }

    public suspend fun deny(invitationId: InvitationId) {
        base.deny(token, invitationId)
    }

    public suspend fun read(): List<AuthorizedInvitationRepository> {
        val invitations = base.read(token).map { it.data }
        return invitations.map { AuthorizedInvitationRepository(it, api) }
    }

    public suspend fun read(ids: List<InvitationId>): List<AuthorizedInvitationRepository> {
        val invitations = base.read(token, ids).map { it.data }
        return invitations.map { AuthorizedInvitationRepository(it, api) }
    }

    @JvmName("readFrom-id")
    public suspend fun read(from: List<UserId>): List<AuthorizedInvitationRepository> {
        val invitations = base.read(token, from).map { it.data }
        return invitations.map { AuthorizedInvitationRepository(it, api) }
    }

    @JvmName("readFrom-user")
    public suspend fun read(from: List<User>): List<AuthorizedInvitationRepository> {
        val invitations = base.read(token, from).map { it.data }
        return invitations.map { AuthorizedInvitationRepository(it, api) }
    }

    @JvmName("readFrom-userRepo")
    public suspend fun read(from: List<UserRepository>): List<AuthorizedInvitationRepository> {
        val invitations = base.read(token, from).map { it.data }
        return invitations.map { AuthorizedInvitationRepository(it, api) }
    }
}
