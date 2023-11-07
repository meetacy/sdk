@file:OptIn(ExperimentalForeignApi::class)

package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.*
import kotlin.coroutines.CoroutineContext
import kotlinx.cinterop.ExperimentalForeignApi

public fun NSURL.asMeetacyOutputSource(
    context: CoroutineContext = Dispatchers.Default
): OutputSource = object : OutputSource {
    override suspend fun open(scope: CoroutineScope): Output {
        val result = runMemScopedCatching { error ->
            NSFileHandle.fileHandleForWritingToURL(this@asMeetacyOutputSource, error.ptr)
        }.asKotlinResult().getOrThrow() ?: error("Cannot open file handle for write")

        return result.asMeetacyOutput(context)
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
                val result = stream.writeData(data, error.ptr)
                require(result) { "Couldn't write data to file handle" }
            }.asKotlinResult().getOrThrow()
        }
    }

    override suspend fun close() = withContext(context) { stream.closeFile() }
}
