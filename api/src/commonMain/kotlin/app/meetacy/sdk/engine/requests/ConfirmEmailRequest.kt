package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.email.ConfirmEmailHash
import app.meetacy.sdk.types.email.ConfirmEmailStatus
import app.meetacy.sdk.types.email.Email

public data class ConfirmEmailRequest(
    val email: Email,
    val confirmHash: ConfirmEmailHash
) : MeetacyRequest<ConfirmEmailRequest.Response> {
    public data class Response(val status: ConfirmEmailStatus)
}
