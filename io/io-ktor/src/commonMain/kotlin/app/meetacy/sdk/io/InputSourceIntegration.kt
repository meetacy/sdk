package app.meetacy.sdk.io

import io.ktor.client.request.forms.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public fun ChannelProvider.asMeetacyInputSource(): InputSource {
    return object : InputSource {

        override suspend fun open(scope: CoroutineScope): Input {
            val channel = block()
            return channel.asMeetacyInput()
        }
    }
}

public fun InputSource.asKtorChannelProvider(
    scope: CoroutineScope,
    size: Long? = null,
    bufferSize: Int = DEFAULT_BUFFER_SIZE
): ChannelProvider {
    return ChannelProvider(
        size = size,
        block = {
            val channel = ByteChannel()

            val job = scope.launch {
                this@asKtorChannelProvider.use { input ->
                    input.asKtorChannel(
                        scope = this,
                        bufferSize = bufferSize
                    ).joinTo(
                        dst = channel,
                        closeOnEnd = true
                    )
                }
            }
            channel.attachJob(job)

            channel
        }
    )
}
