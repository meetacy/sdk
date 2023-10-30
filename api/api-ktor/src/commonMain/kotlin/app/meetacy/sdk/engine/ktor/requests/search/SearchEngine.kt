package app.meetacy.sdk.engine.ktor.requests.search

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.SearchRequest
import app.meetacy.sdk.types.serializable.location.LocationSerializable
import app.meetacy.sdk.types.serializable.location.serializable
import app.meetacy.sdk.types.serializable.search.SearchItemSerializable
import app.meetacy.sdk.types.serializable.search.type
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

internal class SearchEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    val baseUrl: Url = baseUrl / "search"

    @Serializable
    private data class SearchBody(val location: LocationSerializable, val promt: String)
    private fun SearchRequest.toBody() = SearchBody(location.serializable(), prompt)

    suspend fun search(request: SearchRequest): SearchRequest.Response {
        val body = request.toBody()
        val response =  httpClient.get(baseUrl.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<List<SearchItemSerializable>>()
        return SearchRequest.Response(response.map { it.type() })
    }
}
