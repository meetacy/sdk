package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class ListFriendsResponseResult (
    
    @SerialName("data")
    val data: List<User>,
    
    @SerialName("nextPagingId")
    val nextPagingId: String? = null

)
