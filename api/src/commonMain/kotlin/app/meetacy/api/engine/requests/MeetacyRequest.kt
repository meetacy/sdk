package app.meetacy.api.engine.requests

public sealed interface MeetacyRequest<T>

public typealias SimpleMeetacyRequest = MeetacyRequest<Unit>
