import app.meetacy.api.MeetacyApi
import app.meetacy.types.amount.amount
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.toList

val api = MeetacyApi(
    baseUrl = "http://localhost:8080",
    httpClient = HttpClient {
        Logging {
            logger = object : Logger {
                override fun log(message: String) = println(message)
            }
            level = LogLevel.NONE
        }
    }
)

suspend fun main() {
    val myApi = api.auth.generateAuthorizedApi(nickname = "Tester Account at ${System.currentTimeMillis()}")
    val me = myApi.getMe()

    val friends = List(15) {
        api.auth.generateAuthorizedApi(nickname = "Tester Friend at ${System.currentTimeMillis()} #$it")
    }.map { friendApi ->
        friendApi to friendApi.getMe()
    }

    for ((friendApi, friend) in friends) {
        myApi.friends.add(friend.id)
        friendApi.friends.add(me.id)
    }

    myApi.friends.flow(chunkSize = 2.amount).collectIndexed { index, value ->
        println("Chunk ${index + 1}:")
        println(value.joinToString("\n") { it.nickname })
        println()
    }
}
