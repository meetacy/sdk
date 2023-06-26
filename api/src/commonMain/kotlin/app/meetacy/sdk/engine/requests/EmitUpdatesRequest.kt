package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import kotlinx.coroutines.flow.FlowCollector
import app.meetacy.sdk.types.update.Update as UpdateType

public class EmitUpdatesRequest(
    public val token: Token,
    override val collector: FlowCollector<Update>
) : FlowMeetacyRequest<EmitUpdatesRequest.Update> {
    public data class Update(val update: UpdateType)
}
