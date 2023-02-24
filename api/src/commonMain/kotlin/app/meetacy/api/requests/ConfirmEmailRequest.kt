package app.meetacy.api.requests

import app.meetacy.sdk.email.ConfirmEmailHash
import app.meetacy.sdk.email.ConfirmEmailResult
import app.meetacy.sdk.email.Email

public data class ConfirmEmailRequest(
    val email: Email,
    val confirmHash: ConfirmEmailHash
) : MeetacyRequest<ConfirmEmailResult>
