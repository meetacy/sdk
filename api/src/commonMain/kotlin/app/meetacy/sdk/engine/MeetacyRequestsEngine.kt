package app.meetacy.sdk.engine

import app.meetacy.sdk.engine.requests.MeetacyRequest
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url

public interface MeetacyRequestsEngine {
    public fun getFileUrl(id: FileId): Url
    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
