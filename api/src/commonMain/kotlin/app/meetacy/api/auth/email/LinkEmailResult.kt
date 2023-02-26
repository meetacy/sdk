package app.meetacy.api.auth.email

import app.meetacy.types.email.ConfirmEmailHash
import app.meetacy.types.email.ConfirmEmailStatus
import app.meetacy.types.email.Email

public class LinkEmailResult(
    private val emailApi: EmailApi,
    private val email: Email
) {
    public suspend fun confirm(confirmHash: ConfirmEmailHash): ConfirmEmailStatus =
        emailApi.confirm(email, confirmHash)
}
