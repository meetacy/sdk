package app.meetacy.sdk.io.buffer

import kotlin.jvm.JvmInline

@JvmInline
public value class ByteBuffer(private val bytes: ByteArray) {
    public companion object {
        public fun ofInt(int: Int): ByteArray =
            byteArrayOf(
                (int shr 24).toByte(),
                (int shr 16).toByte(),
                (int shr 8).toByte(),
                int.toByte()
            )
    }

    public fun toInt(): Int {
        require(bytes.size == Int.SIZE_BYTES) { "Bytes have size ${bytes.size}, but ${Int.SIZE_BYTES} was expected" }
        return (bytes[0].toInt() shl 24) or
                (bytes[1].toInt() shl 16) or
                (bytes[2].toInt() shl 8) or
                (bytes[3].toInt() shl 0)
    }

    public fun unsafeByteArray(): ByteArray = bytes
    public fun toByteArray(): ByteArray = bytes.copyOf()
}
