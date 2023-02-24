package app.meetacy.api.requests

import app.meetacy.types.email.Email

public data class LinkEmailRequest(
    val email: Email
) : SimpleMeetacyRequest
