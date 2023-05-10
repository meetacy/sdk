package app.meetacy.sdk

import app.meetacy.sdk.engine.ktor.KtorMeetacyEngine
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.url.url
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.*
import kotlinx.serialization.json.Json

public fun MeetacyApi(
    baseUrl: Url,
    httpClient: HttpClient = HttpClient(),
    json: Json = Json
): MeetacyApi = MeetacyApi(
    engine = KtorMeetacyEngine(
        baseUrl = baseUrl,
        httpClient = httpClient,
        json = json
    )
)

public fun MeetacyApi.Companion.production(
    httpClient: HttpClient = HttpClient(),
    enableLogging: Boolean = false,
    json: Json = Json
): MeetacyApi {
    val configuredClient = if (enableLogging) {
        httpClient.config {
            Logging {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    } else httpClient

    return MeetacyApi(
        httpClient = configuredClient,
        json = json,
        baseUrl = "https://api.meetacy.app".url
    )
}
