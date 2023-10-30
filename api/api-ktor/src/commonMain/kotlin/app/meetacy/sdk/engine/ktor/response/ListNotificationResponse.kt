package app.meetacy.sdk.engine.ktor.response

import app.meetacy.sdk.types.serializable.notification.NotificationSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ListNotificationsResponse (
    @SerialName("data")
    val data: List<NotificationSerializable>,
    @SerialName("nextPagingId")
    val nextPagingId: String? = null
)
