package app.meetacy.sdk.files

import app.meetacy.sdk.io.asMeetacyInputSource
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import org.w3c.files.Blob

public suspend inline fun FilesApi.upload(
    token: Token,
    source: Blob,
    crossinline onUpdate: (uploaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
): FileId {
    val fileSize = source.size.toLong()

    return upload(
        token = token,
        source = UploadableFile(
            size = fileSize,
            input = source.asMeetacyInputSource().withCallback { read -> onUpdate(read, fileSize) }
        )
    )
}
