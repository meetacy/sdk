package app.meetacy.api.auth

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.MeetacyApi
import app.meetacy.api.auth.email.EmailApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.GenerateAuthRequest
import app.meetacy.types.auth.Token

public class AuthApi(
    private val engine: MeetacyRequestsEngine,
    private val meetacyApi: MeetacyApi
) {
    public val email: EmailApi = EmailApi(engine)

    public suspend fun generate(nickname: String): Token {
        return engine.execute(GenerateAuthRequest(nickname)).token
    }

    public suspend fun generateAuthorizedApi(nickname: String): AuthorizedMeetacyApi {
        return meetacyApi.authorized(generate(nickname))
    }
}
