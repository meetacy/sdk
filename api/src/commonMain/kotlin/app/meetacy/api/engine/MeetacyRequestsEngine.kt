package app.meetacy.api.engine

import app.meetacy.api.engine.requests.MeetacyRequest
import app.meetacy.types.file.FileId
import app.meetacy.types.url.Url

public interface MeetacyRequestsEngine {
    public fun getFileUrl(id: FileId): Url
    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
