package app.meetacy.sdk.io

import app.meetacy.sdk.io.annotation.IODslMarker
import app.meetacy.sdk.io.bytes.ByteArrayView

@IODslMarker
public interface Output {
    public suspend fun write(source: ByteArrayView)
    public suspend fun close()
}

public suspend inline fun Output.writeIterative(block: () -> ByteArrayView?) {
    while (true) {
        val bytes = block() ?: break
        write(bytes)
    }
}
