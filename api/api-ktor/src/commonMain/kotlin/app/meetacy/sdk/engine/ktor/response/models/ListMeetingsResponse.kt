package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class ListMeetingsResponse (
    
    @SerialName("result")
    val result: ListMeetingsResponseResult,
    
    @SerialName("status")
    val status: Boolean? = null

)
