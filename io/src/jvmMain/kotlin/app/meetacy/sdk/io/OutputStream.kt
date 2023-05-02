package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import kotlin.coroutines.CoroutineContext

public fun OutputStream.asMeetacyOutput(
    context: CoroutineContext = Dispatchers.IO
): Output = object : Output {
    private val stream = this@asMeetacyOutput

    override suspend fun write(source: ByteArrayView) = withContext(context) {
        stream.write(source.underlying, source.fromIndex, source.size)
    }

    override suspend fun close() = withContext(context) {
        stream.close()
    }
}
