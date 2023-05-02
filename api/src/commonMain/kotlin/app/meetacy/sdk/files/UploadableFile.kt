package app.meetacy.sdk.files

import app.meetacy.sdk.io.InputSource

public data class UploadableFile(
    public val size: Long,
    public val input: InputSource
)
