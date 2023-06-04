package app.meetacy.sdk.files

import app.meetacy.sdk.io.InputSource
import app.meetacy.sdk.io.OutputSource
import app.meetacy.sdk.io.asMeetacyInput
import app.meetacy.sdk.io.asMeetacyInputSource
import app.meetacy.sdk.io.asMeetacyOutput
import app.meetacy.sdk.io.asMeetacyOutputSource
import app.meetacy.sdk.io.size
import app.meetacy.sdk.io.transferTo
import app.meetacy.sdk.io.use
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileHandle

public suspend inline fun FilesApi.download(
    fileId: FileId,
    destination: NSFileHandle,
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
    crossinline destination: suspend () -> NSFileHandle,
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
    source: NSFileHandle,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId {
    val fileSize = withContext(Dispatchers.Default) { source.size }

    return upload(
        token = token,
        source = UploadableFile(
            size = fileSize.toLong(),
            input = source.asMeetacyInputSource().withCallback { read -> onUpdate(read, fileSize.toLong()) }
        )
    )
}

public suspend inline fun FilesApi.upload(
    token: Token,
    fileSize: Long,
    crossinline source: suspend () -> NSFileHandle,
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
