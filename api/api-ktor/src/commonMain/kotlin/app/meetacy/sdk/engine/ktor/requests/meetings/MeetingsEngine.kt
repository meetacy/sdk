package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.mapToMeeting
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.response.models.CreateMeetingResponse
import app.meetacy.sdk.engine.ktor.response.models.EditMeetingResponse
import app.meetacy.sdk.engine.ktor.response.models.ListMeetingsResponse
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.MeetingsApiImpl
import dev.icerock.moko.network.generated.models.ListMapMeetingsResponse
import dev.icerock.moko.network.generated.models.ListMeetingParticipantsResponse
import dev.icerock.moko.network.generated.models.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import app.meetacy.sdk.engine.ktor.response.models.Meeting as LolMeeting
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting

internal class MeetingsEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    json: Json
) {
    private val base = MeetingsApiImpl(baseUrl.string, httpClient, json)

    suspend fun listMeetingsHistory(
        request: ListMeetingsHistoryRequest
    ): ListMeetingsHistoryRequest.Response = with(request) {
        val url = baseUrl / "meetings" / "history" / "list"

        val jsonObject = buildJsonObject {
            put("amount", amount.int.toString())
            put("pagingId", pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<ListMeetingsResponse>(string)

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map(LolMeeting::mapToMeeting)
        )

        return ListMeetingsHistoryRequest.Response(paging)
    }

    suspend fun listActiveMeetings(
        request: ListActiveMeetingsRequest
    ): ListActiveMeetingsRequest.Response = with(request) {
        val url = baseUrl / "meetings" / "history" / "active"

        val jsonObject = buildJsonObject {
            put("amount", amount.int.toString())
            put("pagingId", pagingId?.string)
        }

        val string = httpClient.get(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
            header("Authorization", request.token.string)
            header("Api-Version", request.apiVersion.int.toString())
        }.body<String>()

        val response = Json.decodeFromString<ListMeetingsResponse>(string).result

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map(LolMeeting::mapToMeeting)
        )

        return ListActiveMeetingsRequest.Response(paging)
    }

    suspend fun listPastMeetings(
        request: ListPastMeetingsRequest
    ): ListPastMeetingsRequest.Response = with(request) {
        val url = baseUrl / "meetings" / "history" / "past"

        val jsonObject = buildJsonObject {
            put("amount", amount.int.toString())
            put("pagingId", pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<ListMeetingsResponse>(string).result

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map(LolMeeting::mapToMeeting)
        )

        return ListPastMeetingsRequest.Response(paging)
    }

    suspend fun listMeetingsMap(
        request: ListMeetingsMapRequest
    ): ListMeetingsMapRequest.Response = with (request) {
        val url = baseUrl / "meetings" / "map" / "list"

        val jsonObject = buildJsonObject {
            putJsonObject("location") {
                put("latitude", location.latitude)
                put("longitude", location.longitude)
            }
        }
        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<ListMapMeetingsResponse>(string).result

        val data = response.map(GeneratedMeeting::mapToMeeting)

        return ListMeetingsMapRequest.Response(data)
    }

    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): CreateMeetingRequest.Response = with(request){
        val url = baseUrl / "meetings" / "create"

        val jsonObject = buildJsonObject {
            put("title", title)
            put("description", description)
            put("date", date.iso8601)
            putJsonObject("location") {
                put("latitude", location.latitude)
                put("longitude", location.longitude)
            }
            put("visibility", visibility.name.lowercase())
            put("avatarId", fileId?.string)
        }

        val string = httpClient.post(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
            header("Authorization", token.string)
            header("Api-Version", request.apiVersion.int.toString())
        }.body<String>()

        val meeting = Json.decodeFromString<CreateMeetingResponse>(string).result

        return CreateMeetingRequest.Response(meeting.mapToMeeting())
    }

    suspend fun editMeeting(request: EditMeetingRequest): EditMeetingRequest.Response = with(request) {
        val url = baseUrl / "meetings" / "edit"

        val jsonObject = buildJsonObject {
            put("meetingId", meetingId.string)

            title.ifPresent { title ->
                put("title", title)
            }
            description.ifPresent { description ->
                put("description", description)
            }
            location.ifPresent { location ->
                putJsonObject("location") {
                    put("latitude", location.latitude)
                    put("longitude", location.longitude)
                }
            }
            date.ifPresent { date ->
                put("date", date.iso8601)
            }
            avatarId.ifPresent { avatarId ->
                put("avatarId", avatarId?.string)
            }
            visibility.ifPresent { visibility ->
                put("visibility", visibility.name.lowercase())
            }
        }

        val string = httpClient.post(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
            header("Authorization", token.string)
            header("Api-Version", apiVersion.int.toString())
        }.body<String>()

        val meeting = Json.decodeFromString<EditMeetingResponse>(string).result

        return EditMeetingRequest.Response(meeting.mapToMeeting())
    }

    suspend fun listMeetingParticipants(
        request: ListMeetingParticipantsRequest
    ): ListMeetingParticipantsRequest.Response {
        val url = baseUrl / "meetings" / "participants" / "list"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
            put("amount", request.amount.int)
            put("pagingId", request.pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<ListMeetingParticipantsResponse>(string).result

        val paging = PagingResponse(
            data = response.data.map(User::mapToUser),
            nextPagingId = response.nextPagingId?.let(::PagingId)
        )

        return ListMeetingParticipantsRequest.Response(paging)
    }

    suspend fun participateMeeting(request: ParticipateMeetingRequest) {
        val url = baseUrl / "meetings" / "participate"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
        }

        post(url.string, jsonObject, httpClient, request)
    }

    suspend fun getMeeting(request: GetMeetingRequest): GetMeetingRequest.Response {
        val url = baseUrl / "meetings" / "get"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = Json.decodeFromString<CreateMeetingResponse>(string).result

        val meeting = response.mapToMeeting()

        return GetMeetingRequest.Response(meeting)
    }
}
