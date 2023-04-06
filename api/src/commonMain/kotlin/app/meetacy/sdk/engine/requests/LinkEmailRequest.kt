package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email

public data class LinkEmailRequest(
    val token: Token,
    val email: Email
) : SimpleMeetacyRequest
