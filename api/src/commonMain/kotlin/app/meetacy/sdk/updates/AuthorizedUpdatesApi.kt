package app.meetacy.sdk.updates

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.update.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class AuthorizedUpdatesApi(
    private val api: AuthorizedMeetacyApi
) {
    public val token: Token get() = api.token
    public val base: UpdatesApi get() = api.base.updates

    public fun flow(): Flow<AuthorizedUpdateRepository> {
        return base.flow(token).map { update ->
            AuthorizedUpdateRepository.of(update.data, api)
        }
    }
}
