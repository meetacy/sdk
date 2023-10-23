package app.meetacy.sdk.engine.ktor.response.models


import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class ListMeetingsResponseResult (
    
    @SerialName("data")
    val data: List<Meeting>,
    
    @SerialName("nextPagingId")
    val nextPagingId: String? = null

)
