package app.meetacy.sdk.engine.requests

import kotlinx.coroutines.flow.FlowCollector

public interface FlowMeetacyRequest<T> : MeetacyRequest<Unit> {
    public val collector: FlowCollector<T>

    public interface Factory<TRequest : FlowMeetacyRequest<T>, T> {
        public fun create(collector: FlowCollector<T>): TRequest
    }
}
