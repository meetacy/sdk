package app.meetacy.sdk.files

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.GetFileRequest
import app.meetacy.sdk.engine.requests.UploadFileRequest
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url

public class FilesApi(private val api: MeetacyApi) {
    public fun getUrl(id: FileId): Url {
        return api.engine.getFileUrl(id)
    }

    public suspend fun get(fileId: FileId): DownloadableFile {
        return api.engine.execute(GetFileRequest(fileId)).file
    }

    public suspend fun upload(
        token: Token,
        source: UploadableFile
    ): FileId {
        return api.engine.execute(UploadFileRequest(token, source)).fileId
    }
}
