package app.meetacy.sdk.engine.ktor.response

import app.meetacy.sdk.types.serializable.user.UserSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ListMeetingParticipantsResponse (
    @SerialName("data")
    val data: List<UserSerializable>,
    @SerialName("nextPagingId")
    val nextPagingId: String? = null
)