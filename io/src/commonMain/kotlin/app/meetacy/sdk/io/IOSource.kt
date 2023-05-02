package app.meetacy.sdk.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

public interface IOSource {
    public val input: InputSource
    public val output: OutputSource

    public suspend fun open(scope: CoroutineScope): IOChannel
}

public suspend inline fun <T> IOSource.use(
    crossinline block: (IOChannel) -> T
): T = coroutineScope {
    val io = open(scope = this)
    try {
        block(io)
    } finally {
        io.close()
    }
}
