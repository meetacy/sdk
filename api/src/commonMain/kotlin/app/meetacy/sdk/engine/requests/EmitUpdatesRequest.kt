package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.update.UpdateId
import kotlinx.coroutines.flow.FlowCollector
import app.meetacy.sdk.types.update.Update as UpdateType

public class EmitUpdatesRequest(
    public val token: Token,
    public val fromId: UpdateId?,
    override val collector: FlowCollector<Update>
) : FlowMeetacyRequest<EmitUpdatesRequest.Update> {
    public data class Update(val update: UpdateType)
}
