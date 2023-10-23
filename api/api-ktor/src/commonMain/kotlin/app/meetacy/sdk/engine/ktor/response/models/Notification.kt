package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName

@Serializable
internal data class Notification (
    
    @SerialName("type")
    val type: Type,
    
    @SerialName("id")
    val id: String,
    
    @SerialName("isNew")
    val isNew: Boolean,
    
    @SerialName("date")
    val date: String,
    
    @SerialName("subscriber")
    val subscriber: User? = null,
    
    @SerialName("meeting")
    val meeting: Meeting? = null,
    
    @SerialName("inviter")
    val inviter: User? = null

) {
    @Serializable
    enum class Type {
    
        @SerialName("subscription")
        SUBSCRIPTION,
    
        @SerialName("meeting_invitation")
        MEETING_INVITATION;
    
    }

}
