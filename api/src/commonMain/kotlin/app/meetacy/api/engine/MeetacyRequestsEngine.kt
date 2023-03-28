package app.meetacy.api.engine

import app.meetacy.api.engine.requests.MeetacyRequest

public interface MeetacyRequestsEngine {
    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
