package app.meetacy.sdk.auth

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.auth.email.EmailApi
import app.meetacy.sdk.engine.requests.GenerateAuthRequest
import app.meetacy.sdk.types.auth.Token

public class AuthApi(private val api: MeetacyApi) {
    public val email: EmailApi = EmailApi(api)

    public suspend fun generate(nickname: String): Token {
        return api.engine.execute(GenerateAuthRequest(nickname)).token
    }

    public suspend fun generateAuthorizedApi(nickname: String): AuthorizedMeetacyApi {
        return api.authorized(generate(nickname))
    }
}
