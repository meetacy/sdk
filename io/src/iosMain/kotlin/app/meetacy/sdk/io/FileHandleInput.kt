package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileHandle
import platform.Foundation.closeFile
import platform.Foundation.readDataOfLength
import platform.posix.memcpy
import kotlin.coroutines.CoroutineContext

public fun NSFileHandle.asMeetacyInput(
    context: CoroutineContext = Dispatchers.Default
): Input = object : Input {
    val stream = this@asMeetacyInput

    override suspend fun read(destination: ByteArrayView): Int = withContext(context) {
        val (result, error) = stream.seek(destination.fromIndex)

        if (error != null) throw error.toException()
        if (result == null || !result) error("the seek could not be performed")

        val data = stream.readDataOfLength(destination.size.toULong())

        val byteArray = ByteArray(data.length.toInt()).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
        }

        return@withContext byteArray.size.coerceAtLeast(minimumValue = 0)
    }

    override suspend fun close() = withContext(context) { stream.closeFile() }
}
