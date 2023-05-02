package app.meetacy.sdk.io

import kotlinx.coroutines.CoroutineScope
import org.w3c.files.Blob

public fun Blob.asMeetacyInputSource(): InputSource = object : InputSource {
    override suspend fun open(scope: CoroutineScope): Input = reader().asInput()
}
