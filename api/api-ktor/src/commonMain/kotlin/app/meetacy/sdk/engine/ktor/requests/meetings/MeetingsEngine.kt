package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.mapToMeeting
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.meeting.Meeting
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.MeetingsApiImpl
import dev.icerock.moko.network.generated.models.*
import dev.icerock.moko.network.generated.models.AccessMeetingIdRequest
import dev.icerock.moko.network.generated.models.ListMeetingsRequest
import dev.icerock.moko.network.generated.models.Location
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import dev.icerock.moko.network.generated.models.CreateMeetingRequest as GeneratedCreateMeetingRequest
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting

internal class MeetingsEngine(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    json: Json
) {
    private val base = MeetingsApiImpl(baseUrl, httpClient, json)

    suspend fun listMeetingsHistory(
        request: ListMeetingsHistoryRequest
    ): ListMeetingsHistoryRequest.Response = with(request) {
        val response = base.meetingsHistoryListPost(
            listMeetingsRequest = ListMeetingsRequest(
                token = token.string,
                amount = amount.int,
                pagingId = pagingId?.string
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        val paging = PagingResponse(
            nextPagingId = response.result.nextPagingId?.let(::PagingId),
            data = response.result.data.map(GeneratedMeeting::mapToMeeting)
        )

        return ListMeetingsHistoryRequest.Response(paging)
    }

    suspend fun listMeetingsMap(
        request: ListMeetingsMapRequest
    ): ListMeetingsMapRequest.Response = with (request) {
        val response = base.meetingsMapListPost(
            listMapMeetingsRequest = ListMapMeetingsRequest(
                token = token.string,
                location = Location(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        val data = response.result.map(GeneratedMeeting::mapToMeeting)

        return ListMeetingsMapRequest.Response(data)
    }

    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): CreateMeetingRequest.Response {
        val response = base.meetingsCreatePost(
            createMeetingRequest = GeneratedCreateMeetingRequest(
                token = request.token.string,
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
                }
            ),
            apiVersion = request.apiVersion.int.toString()
        ).result

        val meeting = response.mapToMeeting()

        return CreateMeetingRequest.Response(meeting)
    }

    suspend fun editMeeting(request: EditMeetingRequest): EditMeetingRequest.Response = with(request) {
        val url = Url(baseUrl) / "meetings" / "edit"

        val meeting = httpClient.post(url.string) {
            setBody(
                buildJsonObject {
                    put("token", token.string)
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
            )

        }.body<Meeting>()

        return EditMeetingRequest.Response(meeting)
    }

    suspend fun participateMeeting(request: ParticipateMeetingRequest) {
        base.meetingsParticipatePost(
            accessMeetingIdRequest = AccessMeetingIdRequest(
                token = request.token.string,
                meetingId = request.meetingId.string
            ),
            apiVersion = request.apiVersion.int.toString()
        )
    }

    suspend fun getMeeting(request: GetMeetingRequest): GetMeetingRequest.Response {
        val response = base.meetingsGetPost(
            accessMeetingIdRequest = AccessMeetingIdRequest(
                token = request.token.string,
                meetingId = request.meetingId.string
            ),
            apiVersion = request.apiVersion.int.toString()
        )

        val meeting = response.result.mapToMeeting()

        return GetMeetingRequest.Response(meeting)
    }
}
