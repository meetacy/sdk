package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.mapToMeeting
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.response.models.*
import app.meetacy.sdk.engine.ktor.response.models.CreateMeetingResponse
import app.meetacy.sdk.engine.ktor.response.models.EditMeetingResponse
import app.meetacy.sdk.engine.ktor.response.models.ListMapMeetingsResponse
import app.meetacy.sdk.engine.ktor.response.models.ListMeetingParticipantsResponse
import app.meetacy.sdk.engine.ktor.response.models.ListMeetingsResponse
import app.meetacy.sdk.engine.ktor.response.models.User as ModelUser
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.engine.requests.CreateMeetingRequest
import app.meetacy.sdk.engine.requests.EditMeetingRequest
import app.meetacy.sdk.engine.requests.ListMeetingParticipantsRequest
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import app.meetacy.sdk.engine.ktor.response.models.Meeting as ModelMeeting

internal class MeetingsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = baseUrl / "meetings"
    suspend fun listMeetingsHistory(
        request: ListMeetingsHistoryRequest
    ): ListMeetingsHistoryRequest.Response = with(request) {
        val url = baseUrl / "history" / "list"

        val jsonObject = buildJsonObject {
            put("amount", amount.int.toString())
            put("pagingId", pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<ListMeetingsResponse>(string).result

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map(ModelMeeting::mapToMeeting)
        )

        return ListMeetingsHistoryRequest.Response(paging)
    }

    suspend fun listActiveMeetings(
        request: ListActiveMeetingsRequest
    ): ListActiveMeetingsRequest.Response = with(request) {
        val url = baseUrl / "history" / "active"

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

        val response = json.decodeFromString<ListMeetingsResponse>(string).result

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map(ModelMeeting::mapToMeeting)
        )

        return ListActiveMeetingsRequest.Response(paging)
    }

    suspend fun listPastMeetings(
        request: ListPastMeetingsRequest
    ): ListPastMeetingsRequest.Response = with(request) {
        val url = baseUrl / "history" / "past"

        val jsonObject = buildJsonObject {
            put("amount", amount.int.toString())
            put("pagingId", pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<ListMeetingsResponse>(string).result

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map(ModelMeeting::mapToMeeting)
        )

        return ListPastMeetingsRequest.Response(paging)
    }

    suspend fun listMeetingsMap(
        request: ListMeetingsMapRequest
    ): ListMeetingsMapRequest.Response = with (request) {
        val url = baseUrl / "map" / "list"

        val jsonObject = buildJsonObject {
            putJsonObject("location") {
                put("latitude", location.latitude)
                put("longitude", location.longitude)
            }
        }
        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<ListMapMeetingsResponse>(string).result

        val data = response.map(ModelMeeting::mapToMeeting)

        return ListMeetingsMapRequest.Response(data)
    }

    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): CreateMeetingRequest.Response = with(request) {
        val url = baseUrl / "create"

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

        val string = post(url.string, jsonObject, httpClient, request)

        val meeting = json.decodeFromString<CreateMeetingResponse>(string).result

        return CreateMeetingRequest.Response(meeting.mapToMeeting())
    }

    suspend fun editMeeting(request: EditMeetingRequest): EditMeetingRequest.Response = with(request) {
        val url = baseUrl / "edit"

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

        val string = post(url.string, jsonObject, httpClient, request)

        val meeting = json.decodeFromString<EditMeetingResponse>(string).result

        return EditMeetingRequest.Response(meeting.mapToMeeting())
    }

    suspend fun listMeetingParticipants(
        request: ListMeetingParticipantsRequest
    ): ListMeetingParticipantsRequest.Response {
        val url = baseUrl / "participants" / "list"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
            put("amount", request.amount.int)
            put("pagingId", request.pagingId?.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<ListMeetingParticipantsResponse>(string).result

        val paging = PagingResponse(
            data = response.data.map(ModelUser::mapToUser),
            nextPagingId = response.nextPagingId?.let(::PagingId)
        )

        return ListMeetingParticipantsRequest.Response(paging)
    }

    suspend fun participateMeeting(request: ParticipateMeetingRequest): StatusTrueResponse {
        val url = baseUrl / "participate"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return Json.decodeFromString<StatusTrueResponse>(string)
    }

    suspend fun getMeeting(request: GetMeetingRequest): GetMeetingRequest.Response {
        val url = baseUrl / "get"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<CreateMeetingResponse>(string).result

        val meeting = response.mapToMeeting()

        return GetMeetingRequest.Response(meeting)
    }
}
