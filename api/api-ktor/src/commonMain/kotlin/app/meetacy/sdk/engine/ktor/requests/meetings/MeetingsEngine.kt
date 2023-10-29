package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.ListMeetingParticipantsResponse
import app.meetacy.sdk.engine.ktor.response.ListMeetingsResponse
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.optional.ifPresent
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import app.meetacy.sdk.types.serializable.amount.AmountSerializable
import app.meetacy.sdk.types.serializable.amount.serializable
import app.meetacy.sdk.types.serializable.datetime.DateSerializable
import app.meetacy.sdk.types.serializable.datetime.serializable
import app.meetacy.sdk.types.serializable.file.FileIdSerializable
import app.meetacy.sdk.types.serializable.file.serializable
import app.meetacy.sdk.types.serializable.location.LocationSerializable
import app.meetacy.sdk.types.serializable.location.serializable
import app.meetacy.sdk.types.serializable.meeting.MeetingIdSerializable
import app.meetacy.sdk.types.serializable.meeting.MeetingSerializable
import app.meetacy.sdk.types.serializable.meeting.serializable
import app.meetacy.sdk.types.serializable.meeting.type
import app.meetacy.sdk.types.serializable.paging.PagingIdSerializable
import app.meetacy.sdk.types.serializable.paging.serializable
import app.meetacy.sdk.types.serializable.user.type
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

internal class MeetingsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    private val baseUrl = baseUrl / "meetings"

    @Serializable
    private data class ListMeetingsPagingBody(
        val amount: AmountSerializable,
        val pagingId: PagingIdSerializable?
    )
    private fun ListMeetingsHistoryRequest.toBody() = ListMeetingsPagingBody(
        amount.serializable(),
        pagingId?.serializable()
    )

    suspend fun listMeetingsHistory(
        request: ListMeetingsHistoryRequest
    ): ListMeetingsHistoryRequest.Response {
        val url = baseUrl / "history" / "list"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<ListMeetingsResponse>()

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map { meeting -> meeting.type() }
        )
        return ListMeetingsHistoryRequest.Response(paging)
    }

    private fun ListActiveMeetingsRequest.toBody() = ListMeetingsPagingBody(
        amount.serializable(),
        pagingId?.serializable()
    )

    suspend fun listActiveMeetings(
        request: ListActiveMeetingsRequest
    ): ListActiveMeetingsRequest.Response {
        val url = baseUrl / "history" / "active"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<ListMeetingsResponse>()

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map { meeting -> meeting.type() }
        )

        return ListActiveMeetingsRequest.Response(paging)
    }

    private fun ListPastMeetingsRequest.toBody() = ListMeetingsPagingBody(
        amount.serializable(),
        pagingId?.serializable()
    )

    suspend fun listPastMeetings(
        request: ListPastMeetingsRequest
    ): ListPastMeetingsRequest.Response {
        val url = baseUrl / "history" / "past"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<ListMeetingsResponse>()

        val paging = PagingResponse(
            nextPagingId = response.nextPagingId?.let(::PagingId),
            data = response.data.map { meeting -> meeting.type() }
        )

        return ListPastMeetingsRequest.Response(paging)
    }

    @Serializable
    private data class ListMeetingsMapBody(
        val latitude: Double,
        val longitude: Double
    )
    private fun ListMeetingsMapRequest.toBody() = ListMeetingsMapBody(location.latitude, location.longitude)

    suspend fun listMeetingsMap(
        request: ListMeetingsMapRequest
    ): ListMeetingsMapRequest.Response {
        val url = baseUrl / "map" / "list"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<List<MeetingSerializable>>()

        val data = response.map { it.type() }

        return ListMeetingsMapRequest.Response(data)
    }

    @Serializable
    private data class CreateMeetingBody(
        val title: String?,
        val date: DateSerializable,
        val location: LocationSerializable,
        val description: String?,
        val visibility: MeetingSerializable.Visibility,
        val fileId: FileIdSerializable?
    )
    private fun CreateMeetingRequest.toBody() = CreateMeetingBody(
        title,
        date.serializable(),
        location.serializable(),
        description,
        visibility.serializable(),
        fileId?.serializable()
    )

    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): CreateMeetingRequest.Response = with(request) {
        val url = baseUrl / "create"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<MeetingSerializable>()
        return CreateMeetingRequest.Response(response.type())
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

        val response = httpClient.post(url.string) {
            setBody(
                TextContent(
                    text = jsonObject.toString(),
                    contentType = ContentType.Application.Json
                )
            )
            header("Authorization", request.token.string)
            header("Api-Version", request.apiVersion.int.toString())
        }.bodyAsSuccess<MeetingSerializable>()

        return EditMeetingRequest.Response(response.type())
    }

    @Serializable
    private data class ListMeetingParticipantsBody(
        val meetingId: MeetingIdSerializable,
        val amount: AmountSerializable,
        val pagingId: PagingIdSerializable?
    )
    private fun ListMeetingParticipantsRequest.toBody() = ListMeetingParticipantsBody(
        meetingId.serializable(),
        amount.serializable(),
        pagingId?.serializable()
    )

    suspend fun listMeetingParticipants(
        request: ListMeetingParticipantsRequest
    ): ListMeetingParticipantsRequest.Response {
        val url = baseUrl / "participants" / "list"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<ListMeetingParticipantsResponse>()

        val paging = PagingResponse(
            data = response.data.map { it.type() },
            nextPagingId = response.nextPagingId?.let(::PagingId)
        )

        return ListMeetingParticipantsRequest.Response(paging)
    }

    @Serializable
    private data class ParticipateMeetingBody(val meetingId: MeetingIdSerializable)
    private fun ParticipateMeetingRequest.toBody() = ParticipateMeetingBody(meetingId.serializable())

    suspend fun participateMeeting(request: ParticipateMeetingRequest): StatusTrueResponse {
        val url = baseUrl / "participate"
        val body = request.toBody()
        return httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<StatusTrueResponse>()
    }

    @Serializable
    private data class GetMeetingBody(val meetingId: MeetingIdSerializable)
    private fun GetMeetingRequest.toBody() = GetMeetingBody(meetingId.serializable())

    suspend fun getMeeting(request: GetMeetingRequest): GetMeetingRequest.Response {
        val url = baseUrl / "get"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<MeetingSerializable>()

        return GetMeetingRequest.Response(response.type())
    }
}
