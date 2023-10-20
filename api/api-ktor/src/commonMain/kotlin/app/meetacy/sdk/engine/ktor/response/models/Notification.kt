/**
* Meetacy Backend API
* _Version 1.0.2 of the `Meetacy` API documentation_. It is recommended to fill in the request body on your own according to the sample provided below. The `Notifications` section is under development. 
*
* OpenAPI spec version: 1.0.2
* 
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package app.meetacy.sdk.engine.ktor.response.models

import dev.icerock.moko.network.generated.models.Meeting
import dev.icerock.moko.network.generated.models.User
import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName


/**
 * 
 * @param type 
 * @param id 
 * @param isNew 
 * @param date 
 * @param subscriber 
 * @param meeting 
 * @param inviter 
 */
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

    /**
    * 
    * Values: "subscription","meeting_invitation"
    */
    @Serializable
    enum class Type {
    
        @SerialName("subscription")
        SUBSCRIPTION,
    
        @SerialName("meeting_invitation")
        MEETING_INVITATION;
    
    }

}

