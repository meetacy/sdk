package app.meetacy.sdk.engine.ktor.requests.search

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.SearchRequest
import app.meetacy.sdk.types.serializable.search.SearchItemSerializable
import app.meetacy.sdk.types.serializable.search.type
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.request.*

internal class SearchEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    val baseUrl: Url = baseUrl / "search"

    suspend fun search(request: SearchRequest): SearchRequest.Response {
        val response =  httpClient.get(baseUrl.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            parameter("latitude", request.location.latitude)
            parameter("longitude", request.location.longitude)
            parameter("prompt", request.prompt)
        }.bodyAsSuccess<List<SearchItemSerializable>>()
        return SearchRequest.Response(response.map { it.type() })
    }
}
