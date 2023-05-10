package app.meetacy.sdk.engine.requests

import kotlinx.coroutines.flow.FlowCollector

public sealed interface FlowMeetacyRequest<T> : SimpleMeetacyRequest {
    public val collector: FlowCollector<T>
}
