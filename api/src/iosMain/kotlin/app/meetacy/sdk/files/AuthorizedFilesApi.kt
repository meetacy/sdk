package app.meetacy.sdk.files

import app.meetacy.sdk.io.*
import app.meetacy.sdk.types.file.FileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public suspend inline fun AuthorizedFilesApi.download(
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

public suspend inline fun AuthorizedFilesApi.download(
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

public suspend inline fun AuthorizedFilesApi.upload(
    source: NSFileHandle,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId {
    val fileSize = withContext(Dispatchers.Default) { source.length() }

    return upload(
        file = UploadableFile(
            size = fileSize,
            input = source.asMeetacyInputSource().withCallback { read -> onUpdate(read, fileSize) }
        )
    )
}

public suspend inline fun AuthorizedFilesApi.upload(
    fileSize: Long,
    crossinline source: suspend () -> NSFileHandle,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId {
    return upload(
        file = UploadableFile(
            size = fileSize,
            input = object : InputSource {
                override suspend fun open(scope: CoroutineScope): Input = source()
                    .asMeetacyInput()
                    .withCallback { read -> onUpdate(read, fileSize) }
            }
        )
    )
}
