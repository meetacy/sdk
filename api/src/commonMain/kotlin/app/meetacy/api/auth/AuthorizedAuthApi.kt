package app.meetacy.api.auth

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.auth.email.AuthorizedEmailApi
import app.meetacy.types.auth.Token

public class AuthorizedAuthApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: AuthApi get() = api.base.auth

    public val email: AuthorizedEmailApi = AuthorizedEmailApi(api)
}
