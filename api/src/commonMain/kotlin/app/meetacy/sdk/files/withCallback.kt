package app.meetacy.sdk.files

import app.meetacy.sdk.io.Input
import app.meetacy.sdk.io.InputSource
import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.coroutines.CoroutineScope

@PublishedApi
internal suspend inline fun InputSource.withCallback(
    crossinline onUpdate: (read: Long) -> Unit
): InputSource = object : InputSource {
    override suspend fun open(scope: CoroutineScope): Input {
        val base = this@withCallback.open(scope)
        return base.withCallback(onUpdate)
    }
}

@PublishedApi
internal suspend inline fun Input.withCallback(
    crossinline onUpdate: (read: Long) -> Unit
): Input = object : Input {
    private val base = this@withCallback
    private var readAccumulator = 0L

    override suspend fun read(destination: ByteArrayView): Int {
        onUpdate(readAccumulator)
        val read = base.read(destination)
        readAccumulator += read
        return read
    }

    override suspend fun close() {
        onUpdate(readAccumulator)
        base.close()
    }
}
