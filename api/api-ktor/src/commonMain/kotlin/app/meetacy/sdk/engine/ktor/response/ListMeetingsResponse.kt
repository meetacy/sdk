package app.meetacy.sdk.engine.ktor.response

import app.meetacy.sdk.types.serializable.meeting.MeetingSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ListMeetingsResponse (
    @SerialName("data")
    val data: List<MeetingSerializable>,
    @SerialName("nextPagingId")
    val nextPagingId: String? = null
)
