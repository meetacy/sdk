package app.meetacy.sdk.engine

import app.meetacy.sdk.engine.requests.FlowMeetacyRequest
import app.meetacy.sdk.engine.requests.MeetacyRequest
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

public interface MeetacyRequestsEngine {
    public fun getFileUrl(id: FileId): Url

    /**
     * The only possible exception to throw is
     * [app.meetacy.sdk.exception.MeetacyException]
     * and its inheritors
     */
    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
