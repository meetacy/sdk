package app.meetacy.sdk.engine.requests

import app.meetacy.types.auth.Token
import app.meetacy.types.user.UserId

public data class DeleteFriendRequest(
    val token: Token,
    val friendId: UserId
) : SimpleMeetacyRequest
