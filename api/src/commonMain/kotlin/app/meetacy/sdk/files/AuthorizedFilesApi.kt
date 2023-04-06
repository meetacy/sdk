package app.meetacy.sdk.files

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url

public class AuthorizedFilesApi(private val api: AuthorizedMeetacyApi) {
    public val token: Token get() = api.token
    public val base: FilesApi get() = api.base.files

    public fun getFileUrl(id: FileId): Url {
        return base.getFileUrl(id)
    }
}
