package app.meetacy.sdk.engine.ktor.response.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class Meeting (
    
    @SerialName("id")
    val id: String,
    
    @SerialName("creator")
    val creator: User,
    
    @SerialName("date")
    val date: String,
    
    @SerialName("location")
    val location: Location,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("participantsCount")
    val participantsCount: Int,
    
    @SerialName("previewParticipants")
    val previewParticipants: List<User>,
    
    @SerialName("isParticipating")
    val isParticipating: Boolean,
    
    @SerialName("visibility")
    val visibility: Visibility,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("avatarId")
    val avatarId: String? = null

) {
    @Serializable
    enum class Visibility {
    
        @SerialName("public")
        PUBLIC,
    
        @SerialName("private")
        PRIVATE;
    
    }

}
