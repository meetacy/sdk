package app.meetacy.sdk.engine.ktor.requests.meetings

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.ListMeetingParticipantsResponse
import app.meetacy.sdk.engine.ktor.response.ListMeetingsResponse
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.optional.map
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
import app.meetacy.sdk.types.serializable.optional.OptionalSerializable
import app.meetacy.sdk.types.serializable.optional.serializable
import app.meetacy.sdk.types.serializable.paging.PagingIdSerializable
import app.meetacy.sdk.types.serializable.paging.serializable
import app.meetacy.sdk.types.serializable.user.type
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

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
        val response = httpClient.get(url.string) {
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
        val location: LocationSerializable
    )
    private fun ListMeetingsMapRequest.toBody() = ListMeetingsMapBody(location.serializable())

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
        val description: String?,
        val date: DateSerializable,
        val location: LocationSerializable,
        val visibility: MeetingSerializable.Visibility,
        val avatarId: FileIdSerializable?
    )
    private fun CreateMeetingRequest.toBody() = CreateMeetingBody(
        title,
        description,
        date.serializable(),
        location.serializable(),
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

    @Serializable
    private data class EditMeetingBody(
        val meetingId: MeetingIdSerializable,
        val title: OptionalSerializable<String> = OptionalSerializable.Undefined,
        val description: OptionalSerializable<String?> = OptionalSerializable.Undefined,
        val location: OptionalSerializable<LocationSerializable> = OptionalSerializable.Undefined,
        val date: OptionalSerializable<DateSerializable> = OptionalSerializable.Undefined,
        val avatarId: OptionalSerializable<FileIdSerializable?> = OptionalSerializable.Undefined,
        val visibility: OptionalSerializable<MeetingSerializable.Visibility> = OptionalSerializable.Undefined
    )

    private fun EditMeetingRequest.toBody() = EditMeetingBody(
        meetingId.serializable(),
        title.serializable(),
        description.serializable(),
        location.map { it.serializable() }.serializable(),
        date.map { it.serializable() }.serializable(),
        avatarId.map { it?.serializable() }.serializable(),
        visibility.map { it.serializable() }.serializable(),
    )

    suspend fun editMeeting(request: EditMeetingRequest): EditMeetingRequest.Response = with(request) {
        val url = baseUrl / "edit"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
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
        }.body<StatusTrueResponse>()
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
