package app.meetacy.sdk.engine.ktor

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.version.ApiVersion
import io.ktor.client.request.*
import io.ktor.http.*

public fun HttpRequestBuilder.apiVersion(apiVersion: ApiVersion) {
    header("Api-Version", apiVersion.int)
}

public fun HttpRequestBuilder.token(token: Token) {
    header(HttpHeaders.Authorization, token.string)
}
