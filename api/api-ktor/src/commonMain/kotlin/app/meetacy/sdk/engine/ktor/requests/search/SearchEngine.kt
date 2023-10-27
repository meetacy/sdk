package app.meetacy.sdk.engine.ktor.requests.search

import app.meetacy.sdk.engine.requests.SearchRequest
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.request.*

internal class SearchEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    val baseUrl: Url = baseUrl / "search"

    suspend fun search(request: SearchRequest){
        httpClient.get(baseUrl.string) {
            request.location
        }
    }
}
