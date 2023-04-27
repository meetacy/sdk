package app.meetacy.sdk.files

import app.meetacy.sdk.io.asMeetacyInputSource
import app.meetacy.sdk.types.file.FileId
import org.w3c.files.Blob

public suspend inline fun AuthorizedFilesApi.upload(
    source: Blob,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId {
    val fileSize = source.size.toLong()
    return upload(
        file = UploadableFile(
            size = fileSize,
            input = source.asMeetacyInputSource().withCallback { read -> onUpdate(read, fileSize) }
        )
    )
}
