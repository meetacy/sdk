package app.meetacy.sdk.example

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.production
import app.meetacy.sdk.types.auth.Token
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

class SharedFactory {

    private val httpClient: HttpClient by lazy {
        HttpClient(createHttpClientEngine()) {
            expectSuccess = false
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String): Unit =
                        println(message)
                }
                level = LogLevel.HEADERS
            }
        }
    }

    private val environment = object : Environment {
        override var currentToken: Token? = null
    }

    private val meetacyApi: MeetacyApi by lazy {
        MeetacyApi.production(httpClient = httpClient)
    }

    fun createRegisterUseCase() = RegisterUseCase(
        environment = environment,
        meetacyApi = meetacyApi
    )

    @Throws(IllegalStateException::class)
    fun createFilesUseCase() = FilesUseCase(
        meetacyApi = environment.currentToken
            ?.let { meetacyApi.authorized(it) }
            ?: error("Unauthorized")
    )
}
