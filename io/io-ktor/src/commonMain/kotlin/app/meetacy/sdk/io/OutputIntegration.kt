package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public fun ByteWriteChannel.asMeetacyOutput(): Output = object : Output {
    override suspend fun write(source: ByteArrayView) {
        this@asMeetacyOutput.writeFully(source.underlying, source.fromIndex, source.size)
    }

    override suspend fun close() {
        this@asMeetacyOutput.close()
    }
}

public fun Output.asKtorChannel(
    scope: CoroutineScope,
    bufferSize: Int = DEFAULT_BUFFER_SIZE
): ByteWriteChannel {
    val byteChannel = ByteChannel()

    val job = scope.launch {
        val buffer = ByteArrayView(bufferSize)

        this@asKtorChannel.writeIterative {
            byteChannel.readMaxBytes(buffer)
            buffer
        }
    }
    byteChannel.attachJob(job)

    return byteChannel
}
