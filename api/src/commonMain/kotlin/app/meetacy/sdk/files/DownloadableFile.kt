package app.meetacy.sdk.files

import app.meetacy.sdk.io.InputSource
import app.meetacy.sdk.types.file.FileId

public data class DownloadableFile(
    public val id: FileId,
    public val size: Long,
    public val input: InputSource
)
