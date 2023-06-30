package app.meetacy.sdk.engine.ktor.requests.invitations

import app.meetacy.sdk.engine.ktor.toInvitation
import app.meetacy.sdk.engine.requests.*
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.InvitationsApi
import dev.icerock.moko.network.generated.apis.InvitationsApiImpl
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.AcceptInvitationRequest as GeneratedAcceptInvitationRequest
import dev.icerock.moko.network.generated.models.CancelInvitationRequest as GeneratedCancelInvitationRequest
import dev.icerock.moko.network.generated.models.CreateInvitationRequest as GeneratedCreateInvitationRequest
import dev.icerock.moko.network.generated.models.DenyInvitationRequest as GeneratedDenyInvitationRequest

internal class InvitationsEngine(
    baseUrl: Url,
    httpClient: HttpClient,
    json: Json
) {
    private val base: InvitationsApi = InvitationsApiImpl(baseUrl.string, httpClient, json)

    suspend fun create(
        request: CreateInvitationRequest
    ): CreateInvitationRequest.Response {
        val response = base.invitationsCreatePost(
            createInvitationRequest = GeneratedCreateInvitationRequest(
                token = request.token.string,
                meetingId = request.meetingId.string,
                userId = request.userId.string
            ),
            apiVersion = request.apiVersion.int.toString()
        ).result

        return CreateInvitationRequest.Response(response.toInvitation())
    }

    suspend fun accept(
        request: AcceptInvitationRequest
    ) {
        base.invitationsAcceptPost(
            apiVersion = request.apiVersion.int.toString(),
            acceptInvitationRequest = GeneratedAcceptInvitationRequest(
                token = request.token.string,
                id = request.invitationId.string
            )
        )
    }

    suspend fun deny(
        request: DenyInvitationRequest
    ) {
        base.invitationsDenyPost(
            apiVersion = request.apiVersion.int.toString(),
            denyInvitationRequest = GeneratedDenyInvitationRequest(
                token = request.token.string,
                id = request.invitationId.string
            )
        )
    }

    suspend fun cancel(
        request: CancelInvitationRequest
    ) {
        base.invitationsCancelDelete(
            apiVersion = request.apiVersion.int.toString(),
            cancelInvitationRequest = GeneratedCancelInvitationRequest(
                id = request.invitationId.string,
                token = request.token.string
            )
        )
    }
}
