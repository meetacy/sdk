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
 * @param result 
 * @param status 
 */
@Serializable
internal data class ListMeetingsResponse (
    
    @SerialName("result")
    val result: ListMeetingsResponseResult,
    
    @SerialName("status")
    val status: Boolean? = null

)

