package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class StatusTrueResponse (
    
    @SerialName("status")
    val status: Boolean

)
