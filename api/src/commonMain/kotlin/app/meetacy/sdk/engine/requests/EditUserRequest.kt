package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.Username

public data class EditUserRequest(
    override val token: Token,
    val nickname: Optional<String>,
    val username: Optional<Username?>,
    val avatarId: Optional<FileId?>
) : MeetacyRequest<EditUserRequest.Response>, MeetacyRequestWithToken<EditUserRequest.Response> {
    public data class Response(val user: SelfUser)
}
