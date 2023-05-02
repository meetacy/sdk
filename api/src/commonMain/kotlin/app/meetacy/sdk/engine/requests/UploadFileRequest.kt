package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.files.UploadableFile
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId

public class UploadFileRequest(
    public val token: Token,
    public val file: UploadableFile
) : MeetacyRequest<UploadFileRequest.Response> {
    public data class Response(val fileId: FileId)
}
