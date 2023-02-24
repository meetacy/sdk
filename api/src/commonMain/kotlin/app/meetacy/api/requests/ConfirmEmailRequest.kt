package app.meetacy.api.requests

import app.meetacy.types.email.ConfirmEmailHash
import app.meetacy.types.email.ConfirmEmailResult
import app.meetacy.types.email.Email

public data class ConfirmEmailRequest(
    val email: Email,
    val confirmHash: ConfirmEmailHash
) : MeetacyRequest<ConfirmEmailResult>
