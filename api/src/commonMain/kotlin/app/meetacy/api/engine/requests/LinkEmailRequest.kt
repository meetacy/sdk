package app.meetacy.api.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.email.Email

public data class LinkEmailRequest(
    val token: Token,
    val email: Email
) : SimpleMeetacyRequest
