package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class Invitation (
    
    @SerialName("identity")
    val identity: String,
    
    @SerialName("invitedUser")
    val invitedUser: User,
    
    @SerialName("inviterUser")
    val inviterUser: User,
    
    @SerialName("meeting")
    val meeting: Meeting,
    
    @SerialName("isAccepted")
    val isAccepted: Boolean? = null

)
