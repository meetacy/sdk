package app.meetacy.sdk.invitations

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.invitation.InvitationId
import app.meetacy.sdk.types.meeting.MeetingId
import app.meetacy.sdk.types.user.User
import app.meetacy.sdk.types.user.UserId
import app.meetacy.sdk.users.UserRepository
import kotlin.jvm.JvmName

public class InvitationsApi(private val api: MeetacyApi) {
    public suspend fun create(
        token: Token,
        invitedUser: User,
        expiryDate: DateTime,
        meetingId: MeetingId
    ): InvitationsRepository {
        val invitation = api.engine.execute(
            request = CreateInvitationRequest(
                token,
                invitedUser,
                expiryDate,
                meetingId
            )
        ).invitation

        return InvitationsRepository(invitation, api)
    }

    public suspend fun accept(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(request = AcceptInvitationRequest(token, invitationId))
    }

    public suspend fun deny(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(request = DenyInvitationRequest(token, invitationId))
    }

    public suspend fun cancel(
        token: Token,
        invitationId: InvitationId
    ) {
        api.engine.execute(request = CancelInvitationRequest(token, invitationId))
    }

    public suspend fun read(
        token: Token
    ): List<InvitationsRepository> {
        val invitations = api.engine.execute(
            request = ReadInvitationRequest(
                token
            )
        ).result

        return invitations.map { InvitationsRepository(it, api) }
    }

    public suspend fun read(
        token: Token,
        ids: List<InvitationId>
    ): List<InvitationsRepository> {
        val invitations = api.engine.execute(
            request = ReadInvitationRequest(token, ids = ids)
        ).result

        return invitations.map { InvitationsRepository(it, api) }
    }

    @JvmName("readFrom-id")
    public suspend fun read(
        token: Token,
        from: List<UserId>
    ): List<InvitationsRepository> {
        val invitations = api.engine.execute(
            request = ReadInvitationRequest(token, from)
        ).result

        return invitations.map { InvitationsRepository(it, api) }
    }

    @JvmName("readFrom-user")
    public suspend fun read(
        token: Token,
        from: List<User>
    ): List<InvitationsRepository> {
        val invitations = api.engine.execute(
            request = ReadInvitationRequest(token, from.map { it.id })
        ).result

        return invitations.map { InvitationsRepository(it, api) }
    }

    @JvmName("readFrom-userRepo")
    public suspend fun read(
        token: Token,
        from: List<UserRepository>
    ): List<InvitationsRepository> {
        val invitations = api.engine.execute(
            request = ReadInvitationRequest(token, from.map { it.data.id })
        ).result

        return invitations.map { InvitationsRepository(it, api) }
    }

    public suspend fun update(
        token: Token,
        id: InvitationId,
        expiryDate: DateTime? = null,
        meetingId: MeetingId? = null
    ): InvitationsRepository {
        val invitation = api.engine.execute(
            request = UpdateInvitationRequest(token, id, expiryDate, meetingId)
        ).result

        return InvitationsRepository(invitation, api)
    }
}
