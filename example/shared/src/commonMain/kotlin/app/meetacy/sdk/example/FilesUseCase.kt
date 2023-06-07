package app.meetacy.sdk.example

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.files.AuthorizedFilesApi
import app.meetacy.sdk.files.DownloadableFile
import app.meetacy.sdk.types.file.FileId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class FilesUseCase internal constructor(
    meetacyApi: AuthorizedMeetacyApi
) {

    private val useCaseScope = CoroutineScope(Dispatchers.Main)
    private val uploadedFileId: MutableStateFlow<String?> = MutableStateFlow(null)

    val filesApi: AuthorizedFilesApi = meetacyApi.files

    fun setupFileId(fileId: String) {
        uploadedFileId.update { fileId }
    }

    fun subscribe(onChange: (DownloadableFile) -> Unit) {
        uploadedFileId.onEach { fileId ->
            val fileIdNotNull = fileId ?: return@onEach
            withContext(Dispatchers.Main) {
                filesApi.get(FileId(fileIdNotNull)).also(onChange)
            }
        }.launchIn(useCaseScope)
    }
}
