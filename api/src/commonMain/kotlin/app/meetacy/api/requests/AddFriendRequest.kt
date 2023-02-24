package app.meetacy.api.requests

import app.meetacy.api.MeetacyApi
import app.meetacy.types.auth.Token
import app.meetacy.types.user.UserId

public data class AddFriendRequest(
    val token: Token,
    val friendId: UserId
) : SimpleMeetacyRequest

public suspend fun MeetacyApi.addFriend(token: Token, friendId: UserId) {
    execute(AddFriendRequest(token, friendId))
}
