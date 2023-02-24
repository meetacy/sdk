package app.meetacy.api

import app.meetacy.api.requests.MeetacyRequest
import app.meetacy.api.updates.MeetacyUpdate
import app.meetacy.api.updates.filter.MeetacyUpdateFilter
import kotlinx.coroutines.flow.Flow

public interface MeetacyApi {
    public fun requestUpdates(vararg filters: MeetacyUpdateFilter): Flow<MeetacyUpdate>
    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
