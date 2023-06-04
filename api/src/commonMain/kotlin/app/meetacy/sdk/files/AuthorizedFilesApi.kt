package app.meetacy.sdk.files

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.io.InputSource
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

public class AuthorizedFilesApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: FilesApi get() = api.base.files

    public fun getFileUrl(id: FileId): Url {
        return base.getUrl(id)
    }

    public suspend fun get(fileId: FileId): DownloadableFile {
        return api.base.files.get(fileId)
    }

    @OptIn(ExperimentalObjCRefinement::class)
    @HiddenFromObjC
    public suspend fun upload(file: UploadableFile): FileId {
        return api.base.files.upload(token, file)
    }
}
