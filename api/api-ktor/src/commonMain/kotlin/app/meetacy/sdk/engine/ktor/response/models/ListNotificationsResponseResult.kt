package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class ListNotificationsResponseResult (
    
    @SerialName("data")
    val data: List<Notification>,
    
    @SerialName("nextPagingId")
    val nextPagingId: String? = null

)
