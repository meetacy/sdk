package app.meetacy.sdk.requests.meetings

import app.meetacy.sdk.internal.Response
import app.meetacy.sdk.internal.meetacyPost
import app.meetacy.sdk.model.AccessIdentity
import app.meetacy.sdk.model.Meeting
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

@Serializable
private data class GetMeetingsBody(val accessIdentity: AccessIdentity)

internal suspend fun HttpClient.getMeetings(accessIdentity: AccessIdentity): List<Meeting> {
    val result = meetacyPost<List<Meeting>>(urlString = "/meetings/list") {
        setBody(GetMeetingsBody(accessIdentity))
    }
    when (result) {
        is Response.Error -> TODO()
        is Response.Success -> TODO()
    }
}
