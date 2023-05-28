package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.closeFile
import platform.Foundation.create
import platform.Foundation.writeData
import kotlin.coroutines.CoroutineContext

public fun NSFileHandle.asMeetacyOutput(
    context: CoroutineContext = Dispatchers.Default,
): Output = object : Output {
    val stream = this@asMeetacyOutput

    override suspend fun write(source: ByteArrayView) = withContext(context) {
        val (result, error) = stream.seek(source.fromIndex)

        if (error != null) throw error.toException()
        if (result == null || !result) error("the seek could not be performed")

        val data = memScoped {
            NSData.create(
                bytes = allocArrayOf(source.underlying),
                length = source.underlying.size.toULong()
            )
        }

        stream.writeData(data)
    }

    override suspend fun close() = withContext(context) { stream.closeFile() }
}
