package app.meetacy.sdk.files

import app.meetacy.sdk.io.*
import kotlinx.coroutines.CoroutineScope
import platform.Foundation.NSFileHandle
import platform.Foundation.NSURL

public suspend inline fun DownloadableFile.download(
    destination: NSURL,
    crossinline onUpdate: (downloaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
) {
    input.use { input ->
        destination.asMeetacyOutputSource().use { output ->
            input.transferTo(output) { written -> onUpdate(written, size) }
        }
    }
}

public suspend inline fun DownloadableFile.download(
    crossinline destination: suspend () -> NSFileHandle,
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
