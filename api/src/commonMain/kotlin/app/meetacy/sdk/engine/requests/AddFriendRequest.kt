package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.UserId

public data class AddFriendRequest(
    val token: Token,
    val friendId: UserId
) : SimpleMeetacyRequest
