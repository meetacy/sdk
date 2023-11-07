package app.meetacy.sdk.types.serializable.invitation

import app.meetacy.sdk.types.invitation.InvitationId
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class InvitationIdSerializable(public val string: String)

public fun InvitationIdSerializable.type(): InvitationId = InvitationId(string)
public fun InvitationId.serializable(): InvitationIdSerializable = InvitationIdSerializable(string)