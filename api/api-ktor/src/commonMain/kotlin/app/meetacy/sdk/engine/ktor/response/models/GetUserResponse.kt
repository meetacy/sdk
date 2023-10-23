package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class GetUserResponse (
    
    @SerialName("status")
    val status: Boolean? = null,
    
    @SerialName("result")
    val result: User? = null

)
