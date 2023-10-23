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
 * @param status 
 * @param errorCode 
 * @param errorMessage 
 */
@Serializable
internal data class InvalidResponse (
    
    @SerialName("status")
    val status: Boolean,
    
    @SerialName("errorCode")
    val errorCode: ErrorCode,
    
    @SerialName("errorMessage")
    val errorMessage: ErrorMessage

) {

    /**
    * 
    * Values: 1
    */
    @Serializable
    enum class ErrorCode {
    
        @SerialName("1")
        _1;
    
    }

    /**
    * 
    * Values: "Please provide a valid token"
    */
    @Serializable
    enum class ErrorMessage {
    
        @SerialName("Please provide a valid token")
        PLEASE_PROVIDE_A_VALID_TOKEN;
    
    }

}
