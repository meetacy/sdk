package app.meetacy.sdk.auth.email

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ConfirmEmailRequest
import app.meetacy.sdk.engine.requests.LinkEmailRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.ConfirmEmailHash
import app.meetacy.sdk.types.email.ConfirmEmailStatus
import app.meetacy.sdk.types.email.Email

public class EmailApi(private val api: MeetacyApi) {
    public suspend fun link(token: Token, email: Email): LinkEmailResult {
        api.engine.execute(LinkEmailRequest(token, email))
        return LinkEmailResult(emailApi = this, email)
    }

    public suspend fun confirm(
        email: Email,
        confirmHash: ConfirmEmailHash
    ): ConfirmEmailStatus {
        return api.engine.execute(ConfirmEmailRequest(email, confirmHash)).status
    }
}
