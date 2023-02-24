package app.meetacy.api.requests

import app.meetacy.types.amount.Amount
import app.meetacy.types.auth.Token
import app.meetacy.types.offset.Offset
import app.meetacy.types.user.User

public data class ListFriendsRequest(
    val token: Token,
    val offset: Offset,
    val amount: Amount
) : MeetacyRequest<ListFriendsRequest.Response> {
    public data class Response(val friends: List<User>)
}
