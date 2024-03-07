package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.search.SearchItem

public data class SearchRequest(
    val prompt: String,
    val token: Token,
    val location: Location?
) : MeetacyRequest<SearchRequest.Response> {
    public data class Response(val items: List<SearchItem>)
}
