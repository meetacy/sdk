package app.meetacy.sdk.io

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope

public interface InputSource {
    public suspend fun open(scope: CoroutineScope): Input
}

public suspend inline fun <T> InputSource.use(
    crossinline block: suspend (Input) -> T
): T = coroutineScope {
    val input = open(scope = this)
    return@coroutineScope try {
        block(input)
    } finally {
        input.close()
        cancel()
    }
}
