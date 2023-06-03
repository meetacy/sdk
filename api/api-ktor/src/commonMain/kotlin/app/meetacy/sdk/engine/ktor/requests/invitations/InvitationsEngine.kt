package app.meetacy.sdk.engine.ktor.requests.invitations

import app.meetacy.sdk.engine.ktor.toInvitation
import app.meetacy.sdk.engine.requests.AcceptInvitationRequest
import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.engine.requests.ReadInvitationRequest
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.InvitationsApi
import dev.icerock.moko.network.generated.apis.InvitationsApiImpl
import io.ktor.client.*
import kotlinx.serialization.json.Json
import dev.icerock.moko.network.generated.models.AcceptInvitationRequest as GeneratedAcceptInvitationRequest
import dev.icerock.moko.network.generated.models.CreateInvitationRequest as GeneratedCreateInvitationRequest
import dev.icerock.moko.network.generated.models.ReadInvitationRequest as GeneratedReadInvitationRequest

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
                token = request.token.string,
                meeting = request.meeting.string,
                invitedUser = request.invitedUser.id.string,
                expiryDate = request.expiryDate.iso8601
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
                invitationIdentity = request.invitationId.string
            )
        )
    }

    suspend fun read(
        request: ReadInvitationRequest
    ): ReadInvitationRequest.Response {
        val response = base.invitationsReadGet(
            apiVersion = request.apiVersion.int.toString(),
            readInvitationRequest = GeneratedReadInvitationRequest(
                token = request.token.string,
                invitorUserIds = request.from?.map { it.string },
                invitationIds = request.ids?.map { it.string }
            )
        ).result

        return ReadInvitationRequest.Response(response.map { it.toInvitation() })
    }
}
