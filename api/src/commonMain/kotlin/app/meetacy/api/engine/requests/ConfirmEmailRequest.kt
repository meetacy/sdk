package app.meetacy.api.engine.requests

import app.meetacy.types.email.ConfirmEmailHash
import app.meetacy.types.email.ConfirmEmailStatus
import app.meetacy.types.email.Email

public data class ConfirmEmailRequest(
    val email: Email,
    val confirmHash: ConfirmEmailHash
) : MeetacyRequest<ConfirmEmailRequest.Response> {
    public data class Response(val status: ConfirmEmailStatus)
}
