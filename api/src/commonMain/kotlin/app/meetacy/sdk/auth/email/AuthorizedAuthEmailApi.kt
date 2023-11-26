package app.meetacy.sdk.auth.email

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email

public class AuthorizedAuthEmailApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: AuthEmailApi get() = api.base.auth.email

    public suspend fun link(email: Email): LinkEmailResult = base.link(token, email)
}
