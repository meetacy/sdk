package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.files.DownloadableFile
import app.meetacy.sdk.types.file.FileId

public data class GetFileRequest(
    public val fileId: FileId
) : MeetacyRequest<GetFileRequest.Response> {
    public data class Response(public val file: DownloadableFile)
}
