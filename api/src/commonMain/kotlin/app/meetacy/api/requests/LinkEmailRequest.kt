package app.meetacy.api.requests

import app.meetacy.sdk.email.Email

public data class LinkEmailRequest(
    val email: Email
) : SimpleMeetacyRequest
