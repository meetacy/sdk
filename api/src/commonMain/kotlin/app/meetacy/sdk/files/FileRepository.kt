package app.meetacy.sdk.files

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url

public fun FileRepository(id: FileId?, api: AuthorizedMeetacyApi): FileRepository? {
    id ?: return null
    return FileRepository(id, api)
}

public fun FileRepository(id: FileId, api: AuthorizedMeetacyApi): FileRepository {
    return FileRepository(id, api.base)
}

public fun FileRepository(id: FileId?, api: MeetacyApi): FileRepository? {
    id ?: return null
    return FileRepository(id, api)
}

public class FileRepository(
    public val id: FileId,
    public val api: MeetacyApi
) {
    public val url: Url get() = api.files.getFileUrl(id)
}
