package app.meetacy.sdk.auth

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.auth.email.AuthorizedAuthEmailApi
import app.meetacy.sdk.types.auth.Token

public class AuthorizedAuthApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: AuthApi get() = api.base.auth

    public val email: AuthorizedAuthEmailApi = AuthorizedAuthEmailApi(api)
}
