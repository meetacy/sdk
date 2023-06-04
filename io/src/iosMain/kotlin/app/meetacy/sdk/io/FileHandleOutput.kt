package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.cinterop.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.*
import kotlin.coroutines.CoroutineContext

public fun NSFileHandle.asMeetacyOutputSource(
    context: CoroutineContext = Dispatchers.Default
): OutputSource = object : OutputSource {
    override suspend fun open(scope: CoroutineScope): Output {
        return this@asMeetacyOutputSource.asMeetacyOutput(context)
    }
}

public fun NSFileHandle.asMeetacyOutput(
    context: CoroutineContext = Dispatchers.Default,
): Output = object : Output {
    val stream = this@asMeetacyOutput

    override suspend fun write(source: ByteArrayView) {
        withContext(context) {
            val data = memScoped {
                NSData.create(
                    bytes = allocArrayOf(source.extractData()),
                    length = source.size.toULong()
                )
            }

            runMemScopedCatching { error ->
                stream.writeData(data, error.ptr)
            }.asKotlinResult().getOrThrow()
        }
    }

    override suspend fun close() = withContext(context) { stream.closeFile() }
}
