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
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    json: Json
) {
    private val base: InvitationsApi = InvitationsApiImpl(baseUrl.string, httpClient, json)

    suspend fun create(
        request: CreateInvitationRequest
    ): CreateInvitationRequest.Response {
        val response = base.invitationsCreatePost(
            createInvitationRequest = GeneratedCreateInvitationRequest(
                meetingId = request.meetingId.string,
                userId = request.userId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            token = request.token.string
        ).result

        return CreateInvitationRequest.Response(response.toInvitation())
    }

    suspend fun accept(
        request: AcceptInvitationRequest
    ) {
        base.invitationsAcceptPost(
            acceptInvitationRequest = GeneratedAcceptInvitationRequest(
                id = request.invitationId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            token = request.token.string
        )
    }

    suspend fun deny(
        request: DenyInvitationRequest
    ) {
        base.invitationsDenyPost(
            denyInvitationRequest = GeneratedDenyInvitationRequest(
                id = request.invitationId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            token = request.token.string
        )
    }

    suspend fun cancel(
        request: CancelInvitationRequest
    ) {
        base.invitationsCancelPost(
            cancelInvitationRequest = GeneratedCancelInvitationRequest(
                id = request.invitationId.string
            ),
            apiVersion = request.apiVersion.int.toString(),
            token = request.token.string
        )
    }
}
