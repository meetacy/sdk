package app.meetacy.sdk.engine.ktor.requests.files

import app.meetacy.sdk.engine.ktor.response.models.GenerateIdentityResponse
import app.meetacy.sdk.engine.requests.GetFileRequest
import app.meetacy.sdk.engine.requests.UploadFileRequest
import app.meetacy.sdk.files.DownloadableFile
import app.meetacy.sdk.io.Input
import app.meetacy.sdk.io.InputSource
import app.meetacy.sdk.io.asKtorChannelProvider
import app.meetacy.sdk.io.asMeetacyInput
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url
import app.meetacy.sdk.types.url.parametersOf
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

internal class FilesEngine(
    private val baseUrl: Url,
    private val httpClient: HttpClient
) {

    suspend fun get(request: GetFileRequest): GetFileRequest.Response {
        val url = baseUrl / "files" / "download" + parametersOf(
            "fileId" to request.fileId.string
        )

        val fileSize = httpClient.head(url.string) {
            header("Api-Version", request.apiVersion.int)
        }
            .headers[HttpHeaders.ContentLength]
            ?.toLongOrNull()
            ?: error("Cannot get file length")

        val file = DownloadableFile(
            id = request.fileId,
            size = fileSize,
            input = object : InputSource {
                override suspend fun open(scope: CoroutineScope): Input {
                    return httpClient.get(url.string) {
                        header("Api-Version", request.apiVersion.int)
                    }
                        .bodyAsChannel()
                        .asMeetacyInput()
                }
            }
        )

        return GetFileRequest.Response(file)
    }

    suspend fun upload(request: UploadFileRequest): UploadFileRequest.Response = coroutineScope {
        val url = baseUrl / "files" / "upload"

        val string = httpClient.submitFormWithBinaryData(
            url = url.string,
            formData = formData {
                append(
                    key = "file",
                    value = request.file.input.asKtorChannelProvider(
                        size = request.file.size,
                        scope = this@coroutineScope
                    ),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"unnamed\"")
                    }
                )
            }
        ) {
            header("Api-Version", request.apiVersion.int)
            header("Authorization", request.token.string)
        }.bodyAsText()

        val response = Json.decodeFromString<GenerateIdentityResponse>(string)

        val fileId = FileId(response.result)

        UploadFileRequest.Response(fileId)
    }
}
