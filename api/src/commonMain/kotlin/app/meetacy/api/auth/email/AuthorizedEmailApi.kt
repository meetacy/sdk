package app.meetacy.api.auth.email

import app.meetacy.types.auth.Token
import app.meetacy.types.email.Email

public class AuthorizedEmailApi(
    public val token: Token,
    public val base: EmailApi
) {
    public suspend fun link(email: Email): LinkEmailResult = base.link(token, email)
}
