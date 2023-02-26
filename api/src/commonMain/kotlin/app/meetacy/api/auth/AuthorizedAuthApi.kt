package app.meetacy.api.auth

import app.meetacy.api.auth.email.AuthorizedEmailApi
import app.meetacy.types.auth.Token

public class AuthorizedAuthApi(
    public val token: Token,
    public val base: AuthApi
) {
    public val email: AuthorizedEmailApi = AuthorizedEmailApi(token, base.email)
}
