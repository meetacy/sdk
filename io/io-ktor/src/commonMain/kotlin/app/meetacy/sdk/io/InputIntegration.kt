package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public fun ByteReadChannel.asMeetacyInput(): Input = object : Input {
    override suspend fun read(destination: ByteArrayView): Int {
        return this@asMeetacyInput.readMaxBytes(destination)
    }

    override suspend fun close() {
        this@asMeetacyInput.cancel()
    }
}

public fun Input.asKtorChannel(
    scope: CoroutineScope,
    bufferSize: Int = DEFAULT_BUFFER_SIZE
): ByteReadChannel {
    val channel = ByteChannel()

    val job = scope.launch {
        this@asKtorChannel.readIterative(bufferSize) { bytes ->
            channel.writeFully(bytes.underlying, bytes.fromIndex, bytes.size)
        }
        channel.close()
    }
    channel.attachJob(job)

    return channel
}
