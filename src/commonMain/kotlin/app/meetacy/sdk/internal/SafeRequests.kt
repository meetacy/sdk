package app.meetacy.sdk.internal

import app.meetacy.sdk.exception.InternalServerException
import app.meetacy.sdk.exception.InternetConnectionException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestBody<T>(
    val status: Boolean,
    val result: T? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null
)

internal suspend inline fun <reified TBody> HttpClient.meetacyPost(
    urlString: String,
    crossinline block: HttpRequestBuilder.() -> Unit
): Response<TBody> {
    val result: Response<TBody> = try {
        val response = post(urlString, block)

        if (response.status != HttpStatusCode.OK)
            throw InternalServerException()

        val body = response.body<RequestBody<TBody>>()

        when (body.status) {
            true -> Response.Success(body as TBody)
            false -> Response.Error(body.errorCode as Int)
        }
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> throw InternetConnectionException(throwable)
            else -> throw InternalServerException()
        }
    }

    return result
}
