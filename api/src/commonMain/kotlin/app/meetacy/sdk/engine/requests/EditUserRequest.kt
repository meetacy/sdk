package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.optional.Optional
import app.meetacy.sdk.types.user.SelfUser

public data class EditUserRequest(
    val token: Token,
    val nickname: Optional<String>,
    val avatarId: Optional<FileId?>
) : MeetacyRequest<EditUserRequest.Response> {
    public data class Response(val user: SelfUser)
}
