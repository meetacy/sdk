package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.js.JsName

public interface IOChannel {
    public val input: Input
    public val output: Output

    public suspend fun close()
}

@JsName("createIOChannel")
public fun IOChannel(): IOChannel = _IOChannel()

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("ClassName")
private class _IOChannel : IOChannel {

    private val channel = Channel<ByteArrayView>()
    private var lastBytes: ByteArrayView? = null

    override val input: Input = _Input()
    override val output: Output = _Output()

    override suspend fun close() {
        channel.close()
    }

    inner class _Input : Input {
        override suspend fun read(destination: ByteArrayView): Int {
            if (channel.isClosedForReceive) error("Input is closed")
            return read(destination, readAcc = 0)
        }

        private suspend fun read(destination: ByteArrayView, readAcc: Int): Int {
            val data = lastBytes ?: channel.receiveCatching().getOrNull() ?: return readAcc
            lastBytes = null

            val maxDataIndex = data.fromIndex + destination.size
            val endDataIndex = data.toIndex.coerceAtMost(maxDataIndex)

            data.underlying.copyInto(
                destination = destination.underlying,
                destinationOffset = destination.fromIndex,
                startIndex = data.fromIndex,
                endIndex = endDataIndex
            )

            val readSize = data.fromIndex - endDataIndex
            val newReadAcc = readAcc + readSize

            return when {
                maxDataIndex == data.toIndex -> newReadAcc
                maxDataIndex > data.toIndex -> {
                    read(
                        destination = destination.adjustBounds(
                            fromIndex = destination.fromIndex + readSize
                        ),
                        readAcc = newReadAcc
                    )
                }
                maxDataIndex < data.toIndex -> {
                    lastBytes = data.adjustBounds(
                        fromIndex = data.fromIndex + readSize
                    )
                    newReadAcc
                }
                else -> throw IllegalStateException()
            }
        }

        override suspend fun close() {
            this@_IOChannel.close()
        }
    }

    inner class _Output : Output {

        override suspend fun write(source: ByteArrayView) {
            if (channel.isClosedForReceive) error("Output is closed")
            channel.send(source)
        }

        override suspend fun close() {
            this@_IOChannel.close()
        }
    }
}
