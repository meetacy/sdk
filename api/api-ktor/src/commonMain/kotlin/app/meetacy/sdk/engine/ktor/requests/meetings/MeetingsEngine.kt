package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.mapToMeeting
import app.meetacy.sdk.engine.ktor.mapToUser
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.engine.requests.EditMeetingRequest
import app.meetacy.sdk.engine.requests.ListMeetingParticipantsRequest
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.MeetingsApiImpl
import dev.icerock.moko.network.generated.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import dev.icerock.moko.network.generated.models.CreateMeetingRequest as GeneratedCreateMeetingRequest
import dev.icerock.moko.network.generated.models.ListMeetingParticipantsRequest as GeneratedListMeetingParticipantsRequest
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
        val response = base.meetingsHistoryListPost(
            listMeetingsRequest = ListMeetingsRequest(
                amount = amount.int,
                pagingId = pagingId?.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map(GeneratedMeeting::mapToMeeting)
        )

        return ListMeetingsHistoryRequest.Response(paging)
    }

    suspend fun listActiveMeetings(
        request: ListActiveMeetingsRequest
    ): ListActiveMeetingsRequest.Response = with(request) {
        val response = base.meetingsHistoryActiveGet(
            listMeetingsRequest = ListMeetingsRequest(
                amount = amount.int,
                pagingId = pagingId?.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map(GeneratedMeeting::mapToMeeting)
        )

        return ListActiveMeetingsRequest.Response(paging)
    }

    suspend fun listPastMeetings(
        request: ListPastMeetingsRequest
    ): ListPastMeetingsRequest.Response = with(request) {
        val response = base.meetingsHistoryPastGet(
            listMeetingsRequest = ListMeetingsRequest(
                amount = amount.int,
                pagingId = pagingId?.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map(GeneratedMeeting::mapToMeeting)
        )

        return ListPastMeetingsRequest.Response(paging)
    }

    suspend fun listMeetingsMap(
        request: ListMeetingsMapRequest
    ): ListMeetingsMapRequest.Response = with (request) {
        val response = base.meetingsMapListPost(
            listMapMeetingsRequest = ListMapMeetingsRequest(
                location = Location(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val data = response.result.map(GeneratedMeeting::mapToMeeting)

        return ListMeetingsMapRequest.Response(data)
    }

    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): CreateMeetingRequest.Response {
        val response = base.meetingsCreatePost(
            createMeetingRequest = GeneratedCreateMeetingRequest(
                title = request.title,
                date = request.date.iso8601,
                location = Location(
                    latitude = request.location.latitude,
                    longitude = request.location.longitude
                ),
                description = request.description,
                visibility = when (request.visibility) {
                    Meeting.Visibility.Public -> GeneratedCreateMeetingRequest.Visibility.PUBLIC
                    Meeting.Visibility.Private -> GeneratedCreateMeetingRequest.Visibility.PRIVATE
                },
                avatarId = request.fileId?.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        ).result

        val meeting = response.mapToMeeting()

        return CreateMeetingRequest.Response(meeting)
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
            header("Authorization", token)
        }.body<String>()

        val meeting = Json.decodeFromString<EditMeetingResponse>(string).result

        return EditMeetingRequest.Response(meeting.mapToMeeting())
    }

    suspend fun listMeetingParticipants(
        request: ListMeetingParticipantsRequest
    ): ListMeetingParticipantsRequest.Response {
        val response = base.meetingsParticipantsListPost(
            listMeetingParticipantsRequest = GeneratedListMeetingParticipantsRequest(
                amount = request.amount.int,
                meetingId = request.meetingId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val paging = PagingResponse(
            data = response.result.data.map(User::mapToUser),
            nextPagingId = response.result.nextPagingId?.let(::PagingId)
        )

        return ListMeetingParticipantsRequest.Response(paging)
    }

    suspend fun participateMeeting(request: ParticipateMeetingRequest) {
        base.meetingsParticipatePost(
            accessMeetingIdRequest = AccessMeetingIdRequest(
                meetingId = request.meetingId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )
    }

    suspend fun getMeeting(request: GetMeetingRequest): GetMeetingRequest.Response {
        val response = base.meetingsGetPost(
            accessMeetingIdRequest = AccessMeetingIdRequest(
                meetingId = request.meetingId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            authorization = request.token.string
        )

        val meeting = response.result.mapToMeeting()

        return GetMeetingRequest.Response(meeting)
    }
}
