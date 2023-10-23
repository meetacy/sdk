package app.meetacy.sdk.engine.ktor.requests.invitations

import app.meetacy.sdk.engine.ktor.requests.extencion.post
import app.meetacy.sdk.engine.ktor.response.models.CreateInvitationResponse
import app.meetacy.sdk.engine.ktor.response.models.StatusTrueResponse
import app.meetacy.sdk.engine.ktor.toInvitation
import app.meetacy.sdk.engine.requests.AcceptInvitationRequest
import app.meetacy.sdk.engine.requests.CancelInvitationRequest
import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.engine.requests.DenyInvitationRequest
import app.meetacy.sdk.types.url.Url
import io.ktor.client.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class InvitationsEngine(
    baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val baseUrl = baseUrl / "invitations"
    suspend fun create(
        request: CreateInvitationRequest
    ): CreateInvitationRequest.Response {
        val url = baseUrl / "create"

        val jsonObject = buildJsonObject {
            put("meetingId", request.meetingId.string)
            put("userId", request.userId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        val response = json.decodeFromString<CreateInvitationResponse>(string).result

        return CreateInvitationRequest.Response(response.toInvitation())
    }

    suspend fun accept(
        request: AcceptInvitationRequest
    ): StatusTrueResponse {
        val url = baseUrl / "accept"

        val jsonObject = buildJsonObject {
            put("invitationId", request.invitationId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<StatusTrueResponse>(string)
    }

    suspend fun deny(
        request: DenyInvitationRequest
    ): StatusTrueResponse {
        val url = baseUrl / "deny"

        val jsonObject = buildJsonObject {
            put("invitationId", request.invitationId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<StatusTrueResponse>(string)
    }

    suspend fun cancel(
        request: CancelInvitationRequest
    ): StatusTrueResponse {
        val url = baseUrl / "cancel"

        val jsonObject = buildJsonObject {
            put("invitationId", request.invitationId.string)
        }

        val string = post(url.string, jsonObject, httpClient, request)

        return json.decodeFromString<StatusTrueResponse>(string)
    }

}
