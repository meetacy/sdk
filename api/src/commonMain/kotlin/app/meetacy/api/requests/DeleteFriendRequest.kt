package app.meetacy.api.requests

import app.meetacy.sdk.auth.Token
import app.meetacy.sdk.user.UserId

public data class DeleteFriendRequest(
    val token: Token,
    val friendId: UserId
) : SimpleMeetacyRequest
