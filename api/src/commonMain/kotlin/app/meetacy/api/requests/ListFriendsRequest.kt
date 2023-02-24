package app.meetacy.api.requests

import app.meetacy.sdk.amount.Amount
import app.meetacy.sdk.auth.Token
import app.meetacy.sdk.offset.Offset
import app.meetacy.sdk.user.User

public data class ListFriendsRequest(
    val token: Token,
    val offset: Offset,
    val amount: Amount
) : MeetacyRequest<ListFriendsRequest.Response> {
    public data class Response(val friends: List<User>)
}
