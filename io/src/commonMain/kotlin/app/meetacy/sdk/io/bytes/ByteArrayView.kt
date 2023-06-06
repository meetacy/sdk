package app.meetacy.sdk.io.bytes

import app.meetacy.sdk.io.buffer.ByteBuffer

/**
 * Mutable view that is allowed to be modified only
 * from [fromIndex] to [toIndex]
 */
public class ByteArrayView(
    public val underlying: ByteArray,
    public val fromIndex: Int,
    public val toIndex: Int
) {
    public constructor(bytes: ByteArray) : this(
        underlying = bytes,
        fromIndex = 0,
        toIndex = bytes.size
    )
    public constructor(size: Int) : this(ByteArray(size))

    init {
        if (fromIndex != 0 || toIndex != 0) {
            require(fromIndex in underlying.indices)
            require(toIndex in 1..underlying.size)
        }
        require(fromIndex <= toIndex)
    }

    public val indices: IntRange get() = fromIndex until toIndex

    public val size: Int get() = toIndex - fromIndex

    public val byteBuffer: ByteBuffer get() = ByteBuffer(extractData())

    public fun extractData(): ByteArray {
        return underlying.copyOfRange(fromIndex, toIndex)
    }

    public fun adjustBounds(
        fromIndex: Int = this.fromIndex,
        toIndex: Int = this.toIndex
    ): ByteArrayView = ByteArrayView(underlying, fromIndex, toIndex)
    
    public operator fun set(index: Int, byte: Byte) {
        require(index < size)
        underlying[fromIndex + index] = byte
    }
    public operator fun get(index: Int): Byte {
        require(index < size)
        return underlying[fromIndex + index]
    }

    public fun setData(bytes: ByteArray) {
        require(bytes.size == size)
        bytes.copyInto(
            destination = underlying,
            destinationOffset = fromIndex
        )
    }

    public operator fun iterator(): ByteIterator = object : ByteIterator() {
        private var currentIndex: Int = fromIndex

        override fun hasNext(): Boolean {
            return currentIndex in indices
        }

        override fun nextByte(): Byte = get(currentIndex++)
    }

    public fun copy(): ByteArrayView = ByteArrayView(underlying.copyOf(), fromIndex, toIndex)

    public companion object {
        public val Empty: ByteArrayView = ByteArrayView(
            underlying = ByteArray(size = 0),
            fromIndex = 0,
            toIndex = 0
        )
    }
}
