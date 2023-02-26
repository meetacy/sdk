package app.meetacy.api.engine.requests

import app.meetacy.api.MeetacyApi
import app.meetacy.api.engine.MeetacyRequestsEngine
import app.meetacy.types.auth.Token
import app.meetacy.types.user.UserId

public data class AddFriendRequest(
    val token: Token,
    val friendId: UserId
) : SimpleMeetacyRequest
