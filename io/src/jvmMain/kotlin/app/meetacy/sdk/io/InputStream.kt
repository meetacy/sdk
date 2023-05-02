package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

public fun InputStream.asMeetacyInput(
    context: CoroutineContext = Dispatchers.IO
): Input = object : Input {
    private val stream = this@asMeetacyInput

    override suspend fun read(destination: ByteArrayView): Int = withContext(context) {
        return@withContext stream
            .read(destination.underlying, destination.fromIndex, destination.size)
            .coerceAtLeast(minimumValue = 0)
    }

    override suspend fun close() = withContext(context) { stream.close() }
}
