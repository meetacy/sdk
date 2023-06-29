package app.meetacy.sdk.updates

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.EmitUpdatesRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.update.UpdateId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

public class UpdatesApi(private val api: MeetacyApi) {
    public fun flow(
        token: Token,
        fromId: UpdateId? = null
    ): Flow<UpdateRepository> {
        return flow {
            api.engine.execute(
                request = EmitUpdatesRequest(
                    token = token,
                    fromId = fromId,
                    collector = this
                )
            )
        }.map { event -> UpdateRepository.of(event.update, api) }
    }
}
