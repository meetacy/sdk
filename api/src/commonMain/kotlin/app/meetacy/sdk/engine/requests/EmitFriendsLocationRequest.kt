package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.UserLocationSnapshot
import app.meetacy.sdk.types.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

public data class EmitFriendsLocationRequest(
    public val token: Token,
    public val selfLocation: Flow<Location>,
    override val collector: FlowCollector<Update>
) : FlowMeetacyRequest<EmitFriendsLocationRequest.Update> {

    public data class Update(val user: UserLocationSnapshot)
}
