package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.mapToMeeting
import app.meetacy.sdk.engine.requests.CreateMeetingRequest
import app.meetacy.sdk.engine.requests.ListMeetingsHistoryRequest
import app.meetacy.sdk.engine.requests.ListMeetingsMapRequest
import app.meetacy.sdk.engine.requests.ParticipateMeetingRequest
import app.meetacy.types.meeting.Meeting
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import dev.icerock.moko.network.generated.apis.MeetingsApiImpl
import dev.icerock.moko.network.generated.models.*
import dev.icerock.moko.network.generated.models.AccessMeetingIdentityRequest
import dev.icerock.moko.network.generated.models.ListMeetingsRequest
import dev.icerock.moko.network.generated.models.Location
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.CreateMeetingRequest as GeneratedCreateMeetingRequest
import dev.icerock.moko.network.generated.models.Meeting as GeneratedMeeting

internal class MeetingsEngine(
    baseUrl: String,
    httpClient: HttpClient,
    json: Json
) {
    private val base = MeetingsApiImpl(baseUrl, httpClient, json)

    suspend fun listMeetingsHistory(
        request: ListMeetingsHistoryRequest
    ): ListMeetingsHistoryRequest.Response = with(request) {
        val response = base.meetingsHistoryListPost(
            listMeetingsRequest = ListMeetingsRequest(
                accessIdentity = token.string,
                amount = amount.int,
                pagingId = pagingId?.string
            )
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
            )
        )

        val data = response.result.map(GeneratedMeeting::mapToMeeting)

        return ListMeetingsMapRequest.Response(data)
    }

    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): CreateMeetingRequest.Response {
        val response = base.meetingsCreatePost(
            createMeetingRequest = GeneratedCreateMeetingRequest(
                accessIdentity = request.token.string,
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
            )
        ).result

        val meeting = response.mapToMeeting()

        return CreateMeetingRequest.Response(meeting)
    }

    suspend fun participateMeeting(request: ParticipateMeetingRequest) {
        base.meetingsParticipatePost(
            accessMeetingIdentityRequest = AccessMeetingIdentityRequest(
                accessIdentity = request.token.string,
                meetingIdentity = request.meetingId.string
            )
        )
    }
}
