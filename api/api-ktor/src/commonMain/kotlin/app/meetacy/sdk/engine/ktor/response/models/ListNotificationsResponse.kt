package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class ListNotificationsResponse (
    
    @SerialName("result")
    val result: ListNotificationsResponseResult,
    
    @SerialName("status")
    val status: Boolean? = null

)
