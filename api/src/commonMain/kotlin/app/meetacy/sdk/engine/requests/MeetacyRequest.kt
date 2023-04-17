package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.version.ApiVersion

public sealed interface MeetacyRequest<T> {
    public val apiVersion: ApiVersion get() = ApiVersion.latest()
}

public typealias SimpleMeetacyRequest = MeetacyRequest<Unit>
