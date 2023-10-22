package app.meetacy.sdk.engine.ktor.requests.extencion

import app.meetacy.sdk.engine.requests.MeetacyRequestWithToken
import app.meetacy.sdk.engine.requests.TokenProviderEmpty
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.json.JsonObject

public suspend inline fun <T, R : MeetacyRequestWithToken<T>> post(
    urlString: String,
    jsonObject: JsonObject,
    httpClient: HttpClient,
    request: R
): String =
    httpClient.post(urlString) {
        setBody(
            TextContent(
                text = jsonObject.toString(),
                contentType = ContentType.Application.Json
            )
        )
        header("Authorization", request.token.string)
        header("Api-Version", request.apiVersion.int.toString())
    }.body<String>()


public suspend inline fun <T, R : TokenProviderEmpty<T>> postWithoutToken(
    url: String,
    jsonObject: JsonObject,
    httpClient: HttpClient,
    request: R
): String {
    return httpClient.post(url) {
        setBody(
            TextContent(
                text = jsonObject.toString(),
                contentType = ContentType.Application.Json
            )
        )
        header("Api-Version", request.apiVersion.int.toString())
    }.body<String>()
}
