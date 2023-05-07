package app.meetacy.sdk.files

import app.meetacy.sdk.io.*
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream

public suspend inline fun FilesApi.download(
    fileId: FileId,
    destination: File,
    crossinline onUpdate: (downloaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
) {
    val file = get(fileId)

    file.input.use { input ->
        destination.asMeetacyOutputSource().use { output ->
            input.transferTo(output) { written ->
                onUpdate(written, file.size)
            }
        }
    }
}

public suspend inline fun FilesApi.download(
    fileId: FileId,
    crossinline destination: suspend () -> OutputStream,
    crossinline onUpdate: (downloaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
) {
    val file = get(fileId)

    file.input.use { input ->
        object : OutputSource {
            override suspend fun open(scope: CoroutineScope) = destination().asMeetacyOutput()
        }.use { output ->
            input.transferTo(output) { written ->
                onUpdate(written, file.size)
            }
        }
    }
}

public suspend inline fun FilesApi.upload(
    token: Token,
    source: File,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId {
    val fileSize = withContext(Dispatchers.IO) { source.length() }
    return upload(
        token = token,
        source = UploadableFile(
            size = fileSize,
            input = source.asMeetacyInputSource().withCallback { read -> onUpdate(read, fileSize) }
        )
    )
}

public suspend inline fun FilesApi.upload(
    token: Token,
    fileSize: Long,
    crossinline source: suspend () -> InputStream,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId = upload(
    token = token,
    source = UploadableFile(
        size = fileSize,
        input = object : InputSource {
            override suspend fun open(scope: CoroutineScope) = source()
                .asMeetacyInput()
                .withCallback { read -> onUpdate(read, fileSize) }
        }
    )
)
