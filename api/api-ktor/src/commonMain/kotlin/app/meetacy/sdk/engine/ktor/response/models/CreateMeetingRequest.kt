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

import kotlinx.serialization.Serializable






import kotlinx.serialization.SerialName


/**
 * 
 * @param title 
 * @param date 
 * @param location 
 * @param visibility 
 * @param avatarId 
 * @param description 
 */
@Serializable
internal data class CreateMeetingRequest (

    @SerialName("title")
    val title: String,

    @SerialName("date")
    val date: String,

    @SerialName("location")
    val location: Location,

    @SerialName("visibility")
    val visibility: Visibility,

    @SerialName("avatarId")
    val avatarId: String? = null,

    @SerialName("description")
    val description: String? = null

) {

    /**
    * 
    * Values: "public","private"
    */
    @Serializable
    enum class Visibility {
    
        @SerialName("public")
        PUBLIC,
    
        @SerialName("private")
        PRIVATE;
    
    }

}
