package app.meetacy.api.files

import app.meetacy.api.MeetacyApi
import app.meetacy.types.file.FileId
import app.meetacy.types.url.Url

public class FilesApi(private val api: MeetacyApi) {
    public fun getFileUrl(id: FileId): Url {
        return api.engine.getFileUrl(id)
    }
}
