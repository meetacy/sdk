package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.friends.location.FriendsOnMapRepository
import app.meetacy.sdk.types.auth.Token
import kotlinx.coroutines.flow.FlowCollector

public data class EmitFriendsLocationRequest(
    public val token: Token,
    override val collector: FlowCollector<Response>
) : FlowMeetacyRequest<EmitFriendsLocationRequest.Response> {

    public data class Response(val map: FriendsOnMapRepository)
}
