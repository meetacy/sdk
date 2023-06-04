package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileHandle
import platform.Foundation.closeFile
import platform.Foundation.readDataOfLength
import platform.Foundation.seekToEndOfFile
import platform.posix.memcpy
import kotlin.coroutines.CoroutineContext

public fun NSFileHandle.asMeetacyInput(
    context: CoroutineContext = Dispatchers.Default
): Input = object : Input {
    val stream = this@asMeetacyInput
    val size = stream.seekToEndOfFile().also { _ ->
        stream.seek(0u)
    }

    override suspend fun read(destination: ByteArrayView): Int = withContext(context) {
        val currentOffset: ULong = stream.getOffset().first ?: error("get current offset error")
        val newOffset: ULong = currentOffset + destination.fromIndex.toULong()
        val (result, error) = stream.seek(newOffset)

        if (error != null) throw error.toException()
        if (result == null || !result) error("the seek could not be performed")

        val remainingLength = (size - newOffset).coerceAtLeast(0u)
        val readLength = destination.size.toULong().coerceAtMost(remainingLength)

        val data = stream.readDataOfLength(readLength)

        val byteArray = ByteArray(data.length.toInt()).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
        }

        return@withContext byteArray.size.coerceAtLeast(minimumValue = 0)
    }

    override suspend fun close() = withContext(context) { stream.closeFile() }
}

public fun NSFileHandle.asMeetacyInputSource(
    context: CoroutineContext = Dispatchers.Default
): InputSource = object : InputSource {
    override suspend fun open(scope: CoroutineScope): Input {
        return this@asMeetacyInputSource.asMeetacyInput(context)
    }
}

public fun NSFileHandle.asMeetacyOutputSource(
    context: CoroutineContext = Dispatchers.Default
): OutputSource = object : OutputSource {
    override suspend fun open(scope: CoroutineScope): Output {
        return this@asMeetacyOutputSource.asMeetacyOutput(context)
    }
}
