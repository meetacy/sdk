import app.meetacy.api.MeetacyApi
import app.meetacy.types.amount.amount

val baseApi: MeetacyApi = TODO()

suspend fun main() {
    val api = baseApi.auth.generateAuthorizedApi(nickname = "Alex Sokol")

    val me = api.getMe()
    val friends = api.friends.list(20.amount)
}
