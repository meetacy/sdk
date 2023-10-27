package app.meetacy.sdk.engine.ktor.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StatusTrueResponse (

    @SerialName("status")
    val status: Boolean

)