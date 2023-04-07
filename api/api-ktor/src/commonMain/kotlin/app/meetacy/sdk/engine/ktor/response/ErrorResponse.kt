package app.meetacy.sdk.engine.ktor.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    val status: Boolean,
    val errorCode: Int,
    val errorMessage: String
)
