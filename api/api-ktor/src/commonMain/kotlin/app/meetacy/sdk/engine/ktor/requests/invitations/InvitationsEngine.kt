package app.meetacy.sdk.engine.ktor.requests.invitations

import app.meetacy.sdk.engine.requests.CreateInvitationRequest
import app.meetacy.sdk.types.url.Url
import dev.icerock.moko.network.generated.apis.InvitationsApi
import dev.icerock.moko.network.generated.apis.InvitationsApiImpl
import dev.icerock.moko.network.generated.models.CreateInvitationRequest as GeneratedCreateInvitationRequest
import io.ktor.client.*
import kotlinx.serialization.json.Json

internal class InvitationsEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    json: Json
) {
    private val base: InvitationsApi = InvitationsApiImpl(baseUrl.string, httpClient, json)

    suspend fun create(request: CreateInvitationRequest) {
        base.invitationsCreatePost(
            createInvitationRequest = GeneratedCreateInvitationRequest(
                token = request.token.string,
                meeting = request.meeting.string,
                invitedUser = request.invitedUser.id.string,
                expiryDate = request.expiryDate.iso8601
            ),
            apiVersion = request.apiVersion.int.toString()
        )
    }
}
