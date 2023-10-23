package app.meetacy.sdk.engine.ktor.response.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
internal data class User (
    
    @SerialName("isSelf")
    val isSelf: Boolean,
    
    @SerialName("id")
    val id: String,
    
    @SerialName("nickname")
    val nickname: String,
    
    @SerialName("username")
    val username: String? = null,
    
    @SerialName("email")
    val email: String? = null,
    
    @SerialName("emailVerified")
    val emailVerified: Boolean? = null,
    
    @SerialName("avatarId")
    val avatarId: String? = null,
    /* Relationship field can be one of four types - subscription, subscriber, friend and none. In case if isSelf == true, relationship field is null */
    
    @SerialName("relationship")
    val relationship: String? = null

)
