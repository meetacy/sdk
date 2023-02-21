package app.meetacy.sdk

import app.meetacy.sdk.model.AccessIdentity
import app.meetacy.sdk.requests.auth.authorize
import app.meetacy.sdk.requests.auth.generateToken
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MeetacyClient internal constructor(baseUrl: String, httpClient: HttpClient) {
    private val httpClient = httpClient.config {
        expectSuccess = false
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            url(baseUrl)
        }
    }

    suspend fun generateToken(nickname: String) = httpClient.generateToken(nickname)
    suspend fun authorize(token: AccessIdentity) = httpClient.authorize(token)

    companion object {
        fun production(enableLogging: Boolean = false) =
            custom(baseUrl = "https://api.meetacy.app", enableLogging)

        fun localHost(port: Int = 8080, enableLogging: Boolean = false) =
            custom(baseUrl = "http://localhost:$port", enableLogging)

        fun custom(baseUrl: String, enableLogging: Boolean = false) = MeetacyClient(
            baseUrl = baseUrl,
            httpClient = HttpClient {
                if (enableLogging) install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }
        )
    }
}
