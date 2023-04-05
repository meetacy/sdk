package app.meetacy.api.auth

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.MeetacyApi
import app.meetacy.api.auth.email.EmailApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.GenerateAuthRequest
import app.meetacy.types.auth.Token

public class AuthApi(private val api: MeetacyApi) {
    public val email: EmailApi = EmailApi(api)

    public suspend fun generate(nickname: String): Token {
        return api.engine.execute(GenerateAuthRequest(nickname)).token
    }

    public suspend fun generateAuthorizedApi(nickname: String): AuthorizedMeetacyApi {
        return api.authorized(generate(nickname))
    }
}
