package app.meetacy.sdk.engine.ktor.requests.invitations

import app.meetacy.sdk.engine.ktor.apiVersion
import app.meetacy.sdk.engine.ktor.response.CreateInvitationResponse
import app.meetacy.sdk.engine.ktor.response.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.response.bodyAsSuccess
import app.meetacy.sdk.engine.ktor.token
import app.meetacy.sdk.engine.requests.AcceptInvitationRequest
import app.meetacy.sdk.engine.requests.CancelInvitationRequest
import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.engine.requests.DenyInvitationRequest
import app.meetacy.sdk.types.serializable.invitation.InvitationIdSerializable
import app.meetacy.sdk.types.serializable.invitation.serializable
import app.meetacy.sdk.types.serializable.invitation.type
import app.meetacy.sdk.types.serializable.meeting.MeetingIdSerializable
import app.meetacy.sdk.types.serializable.meeting.serializable
import app.meetacy.sdk.types.serializable.user.UserIdSerializable
import app.meetacy.sdk.types.serializable.user.serializable
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

internal class InvitationsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient
) {
    private val baseUrl = baseUrl / "invitations"

    @Serializable
    private data class CreateInvitationBody(
        val meetingId: MeetingIdSerializable,
        val userId: UserIdSerializable
    )
    private fun CreateInvitationRequest.toBody() = CreateInvitationBody(
        meetingId.serializable(),
        userId.serializable()
    )

    suspend fun create(
        request: CreateInvitationRequest
    ): CreateInvitationRequest.Response {
        val url = baseUrl / "create"
        val body = request.toBody()
        val response = httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.bodyAsSuccess<CreateInvitationResponse>().result
        return CreateInvitationRequest.Response(response.type())
    }

    @Serializable
    private data class AcceptInvitationBody(val invitationId: InvitationIdSerializable)
    private fun AcceptInvitationRequest.toBody() = AcceptInvitationBody(invitationId.serializable())

    suspend fun accept(
        request: AcceptInvitationRequest
    ): StatusTrueResponse {
        val url = baseUrl / "accept"
        val body = request.toBody()
        return httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
    }

    @Serializable
    private data class DenyInvitationBody(val invitationId: InvitationIdSerializable)
    private fun DenyInvitationRequest.toBody() = DenyInvitationBody(invitationId.serializable())

    suspend fun deny(
        request: DenyInvitationRequest
    ): StatusTrueResponse {
        val url = baseUrl / "deny"
        val body = request.toBody()
        return httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
    }

    @Serializable
    private data class CancelInvitationBody(val invitationId: InvitationIdSerializable)
    private fun CancelInvitationRequest.toBody() = CancelInvitationBody(invitationId.serializable())

    suspend fun cancel(
        request: CancelInvitationRequest
    ): StatusTrueResponse {
        val url = baseUrl / "cancel"
        val body = request.toBody()
        return httpClient.post(url.string) {
            apiVersion(request.apiVersion)
            token(request.token)
            setBody(body)
        }.body<StatusTrueResponse>()
    }
}
