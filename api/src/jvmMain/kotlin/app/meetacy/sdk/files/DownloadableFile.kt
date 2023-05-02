package app.meetacy.sdk.files

import app.meetacy.sdk.io.*
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.OutputStream

public suspend inline fun DownloadableFile.download(
    destination: File,
    crossinline onUpdate: (downloaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
) {
    input.use { input ->
        destination.asMeetacyOutputSource().use { output ->
            input.transferTo(output) { written -> onUpdate(written, size) }
        }
    }
}

public suspend inline fun DownloadableFile.download(
    crossinline destination: suspend () -> OutputStream,
    crossinline onUpdate: (downloaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
) {
    input.use { input ->
        object : OutputSource {
            override suspend fun open(scope: CoroutineScope) = destination().asMeetacyOutput()
        }.use { output ->
            input.transferTo(output) { written -> onUpdate(written, size) }
        }
    }
}
