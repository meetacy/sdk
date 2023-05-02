package app.meetacy.sdk.io

import app.meetacy.sdk.io.bytes.ByteArrayView
import io.ktor.utils.io.*

public suspend fun ByteReadChannel.readMaxBytes(destination: ByteArrayView): Int {
    return readMaxBytes(
        destination = destination,
        readAcc = 0
    )
}

private suspend fun ByteReadChannel.readMaxBytes(destination: ByteArrayView, readAcc: Int): Int {
    val readSize = this.readAvailable(
        dst = destination.underlying,
        offset = destination.fromIndex + readAcc,
        length = destination.size - readAcc
    )

    if (readSize <= 0) return readAcc

    return readMaxBytes(destination, readAcc = readAcc + readSize)
}
