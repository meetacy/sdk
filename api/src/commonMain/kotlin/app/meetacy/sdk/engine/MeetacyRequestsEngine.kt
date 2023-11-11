@file:OptIn(UnstableApi::class)

package app.meetacy.sdk.engine

import app.meetacy.sdk.engine.requests.MeetacyRequest
import app.meetacy.sdk.types.annotation.UnstableApi
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url

public interface MeetacyRequestsEngine {
    public fun getFileUrl(id: FileId): Url

    /**
     * The only possible exception to throw is
     * [app.meetacy.sdk.exception.MeetacyException]
     * and its inheritors
     */
    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
