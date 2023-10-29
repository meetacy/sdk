package app.meetacy.sdk.engine.ktor.response

import app.meetacy.sdk.types.serializable.invitation.InvitationSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateInvitationResponse (
    @SerialName("status")
    val status: Boolean,
    @SerialName("result")
    val result: InvitationSerializable
)
