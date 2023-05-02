package app.meetacy.sdk.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

public interface OutputSource {
    public suspend fun open(scope: CoroutineScope): Output
}

public suspend inline fun <T> OutputSource.use(
    crossinline block: suspend (Output) -> T
): T = coroutineScope {
    val output = open(scope = this)
    return@coroutineScope try {
        block(output)
    } finally {
        output.close()
    }
}
