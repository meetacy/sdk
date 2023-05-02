package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import kotlinx.coroutines.await

internal fun Reader.asInput(): Input = object : Input {
    private var lastBytes: ByteArrayView? = null

    override suspend fun read(destination: ByteArrayView): Int {
        return read(destination, readAcc = 0)
    }

    private tailrec suspend fun read(destination: ByteArrayView, readAcc: Int): Int {
        val bytes = lastBytes ?: ByteArrayView(
            bytes = read().await().value ?: return readAcc
        )
        lastBytes = null

        val maxEndIndex = bytes.fromIndex + destination.size
        val endIndex = bytes.toIndex.coerceAtMost(maxEndIndex)

        bytes.underlying.copyInto(
            destination = destination.underlying,
            destinationOffset = destination.fromIndex,
            startIndex = bytes.fromIndex,
            endIndex = endIndex
        )

        val readSize = endIndex - bytes.fromIndex

        return when {
            maxEndIndex == bytes.toIndex -> readAcc + readSize
            maxEndIndex > bytes.toIndex -> read(
                destination = destination.adjustBounds(
                    fromIndex = destination.fromIndex + readSize
                ),
                readAcc = readAcc + readSize
            )
            maxEndIndex < bytes.toIndex -> {
                lastBytes = bytes.adjustBounds(
                    fromIndex = bytes.fromIndex + readSize
                )
                readAcc + readSize
            }
            else -> error("Stub")
        }
    }

    override suspend fun close() {
        lastBytes = null
        this@asInput.cancel()
    }
}


