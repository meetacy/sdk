package app.meetacy.api.engine

import app.meetacy.api.engine.requests.MeetacyRequest
import app.meetacy.api.engine.updates.MeetacyUpdate
import app.meetacy.api.engine.updates.filter.MeetacyUpdateFilter
import app.meetacy.types.auth.Token
import app.meetacy.types.update.UpdateId
import kotlinx.coroutines.flow.Flow

public interface MeetacyRequestsEngine {
    public fun updatesPolling(
        token: Token,
        vararg filters: MeetacyUpdateFilter<*>,
        lastUpdateId: UpdateId? = null
    ): Flow<MeetacyUpdate>

    public suspend fun <T> execute(request: MeetacyRequest<T>): T
}
