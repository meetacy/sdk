package app.meetacy.sdk.auth.email

import app.meetacy.sdk.types.email.ConfirmEmailHash
import app.meetacy.sdk.types.email.ConfirmEmailStatus
import app.meetacy.sdk.types.email.Email

public class LinkEmailResult(
    private val emailApi: EmailApi,
    private val email: Email
) {
    public suspend fun confirm(confirmHash: ConfirmEmailHash): ConfirmEmailStatus =
        emailApi.confirm(email, confirmHash)
}
