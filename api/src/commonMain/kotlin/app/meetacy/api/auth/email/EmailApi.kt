package app.meetacy.api.auth.email

import app.meetacy.api.MeetacyApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.api.engine.requests.ConfirmEmailRequest
import app.meetacy.api.engine.requests.LinkEmailRequest
import app.meetacy.types.auth.Token
import app.meetacy.types.email.ConfirmEmailHash
import app.meetacy.types.email.ConfirmEmailStatus
import app.meetacy.types.email.Email

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
