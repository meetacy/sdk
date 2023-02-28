package app.meetacy.api

import app.meetacy.api.engine.ktor.KtorMeetacyEngine
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

public fun MeetacyApi(
    baseUrl: String,
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
    json: Json = Json
): MeetacyApi = MeetacyApi(
    httpClient = httpClient,
    json = json,
    baseUrl = "https://api.meetacy.app"
)
