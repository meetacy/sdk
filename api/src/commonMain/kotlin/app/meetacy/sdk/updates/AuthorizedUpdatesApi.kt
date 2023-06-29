package app.meetacy.sdk.updates

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.update.Update
import app.meetacy.sdk.types.update.UpdateId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class AuthorizedUpdatesApi(
    private val api: AuthorizedMeetacyApi
) {
    public val token: Token get() = api.token
    public val base: UpdatesApi get() = api.base.updates

    public fun flow(fromId: UpdateId? = null): Flow<AuthorizedUpdateRepository> {
        return base.flow(token, fromId).map { update ->
            AuthorizedUpdateRepository.of(update.data, api)
        }
    }
}
