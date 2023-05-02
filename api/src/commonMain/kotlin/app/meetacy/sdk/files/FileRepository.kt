package app.meetacy.sdk.files

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.io.Input
import app.meetacy.sdk.io.InputSource
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.url.Url
import kotlinx.coroutines.CoroutineScope

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
    public val url: Url get() = api.files.getUrl(id)

    public val input: InputSource get() = object : InputSource {
        override suspend fun open(scope: CoroutineScope): Input {
            val file = api.files.get(id)
            return file.input.open(scope)
        }
    }
}
