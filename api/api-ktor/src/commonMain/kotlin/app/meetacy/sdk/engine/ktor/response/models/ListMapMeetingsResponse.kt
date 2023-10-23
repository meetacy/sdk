package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class ListMapMeetingsResponse (
    
    @SerialName("result")
    val result: List<Meeting>,
    
    @SerialName("status")
    val status: Boolean? = null

)
