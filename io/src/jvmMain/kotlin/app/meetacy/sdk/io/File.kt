package app.meetacy.sdk.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import kotlin.coroutines.CoroutineContext

public fun File.asMeetacyInputSource(
    context: CoroutineContext = Dispatchers.IO
): InputSource = object : InputSource {
    override suspend fun open(scope: CoroutineScope): Input {
        return inputStream().asMeetacyInput(context)
    }
}

public fun File.asMeetacyOutputSource(
    context: CoroutineContext = Dispatchers.IO
): OutputSource = object : OutputSource {
    override suspend fun open(scope: CoroutineScope): Output {
        return outputStream().asMeetacyOutput(context)
    }
}
