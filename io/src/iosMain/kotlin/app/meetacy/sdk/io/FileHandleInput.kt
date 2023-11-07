@file:OptIn(ExperimentalForeignApi::class)

package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileHandle
import platform.Foundation.NSURL
import platform.Foundation.closeFile
import platform.Foundation.fileHandleForReadingFromURL
import platform.posix.memcpy
import kotlin.coroutines.CoroutineContext
import kotlinx.cinterop.ExperimentalForeignApi

public fun NSURL.asMeetacyInputSource(
    context: CoroutineContext = Dispatchers.Default
): InputSource = object : InputSource {
    override suspend fun open(scope: CoroutineScope): Input {
        val handle = runMemScopedCatching { error ->
            NSFileHandle.fileHandleForReadingFromURL(this@asMeetacyInputSource, error.ptr)
        }.asKotlinResult().getOrThrow() ?: error("Cannot open file handle for read")

        return handle.asMeetacyInput(context)
    }
}

public fun NSFileHandle.asMeetacyInput(
    context: CoroutineContext = Dispatchers.Default
): Input = object : Input {
    val stream = this@asMeetacyInput

    override suspend fun read(destination: ByteArrayView): Int = withContext(context) {
        val data = runMemScopedCatching { error ->
            stream.readDataUpToLength(destination.size.toULong(), error.ptr)
        }.asKotlinResult().getOrThrow() ?: error("Returned null data while reading")

        destination.apply {
            underlying.usePinned { pinned ->
                memcpy(pinned.addressOf(fromIndex), data.bytes, toIndex.toULong())
            }
        }

        return@withContext data.length.toInt()
    }

    override suspend fun close() = withContext(context) { stream.closeFile() }
}
