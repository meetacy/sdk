package app.meetacy.api.auth.email

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.types.auth.Token
import app.meetacy.types.email.Email

public class AuthorizedEmailApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: EmailApi get() = api.base.auth.email

    public suspend fun link(email: Email): LinkEmailResult = base.link(token, email)
}
