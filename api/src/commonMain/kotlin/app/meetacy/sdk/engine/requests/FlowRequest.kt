package app.meetacy.sdk.engine.requests

import kotlinx.coroutines.flow.FlowCollector

public interface FlowMeetacyRequest<T> : MeetacyRequest<Unit> {
    public val collector: FlowCollector<T>
}
