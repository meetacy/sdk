package app.meetacy.sdk.io

import app.meetacy.sdk.io.annotation.IODslMarker
import app.meetacy.sdk.io.bytes.ByteArrayView

@IODslMarker
public interface Input {
    public suspend fun read(destination: ByteArrayView): Int
    public suspend fun close()
}

public suspend inline fun Input.readIterative(buffer: ByteArrayView, consume: (bytes: ByteArrayView) -> Unit) {
    while (true) {
        val readSize = read(buffer)
        val newBuffer = buffer.adjustBounds(toIndex = buffer.fromIndex + readSize)
        consume(newBuffer)
        if (readSize != buffer.size) break
    }
}

public suspend inline fun Input.readIterative(
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    consume: (bytes: ByteArrayView) -> Unit
) {
    return readIterative(ByteArrayView(bufferSize), consume)
}

public suspend inline fun Input.transferTo(
    output: Output,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    onUpdate: (written: Long) -> Unit = {}
) {
    var acc = 0L
    this.readIterative(bufferSize) { bytes ->
        output.write(bytes)
        acc += bytes.size
        onUpdate(acc)
    }
}
