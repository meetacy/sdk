package app.meetacy.sdk.offset

import app.meetacy.sdk.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Offset @UnsafeConstructor constructor(public val int: Int) {
    @OptIn(UnsafeConstructor::class)
    public companion object {
        public fun parse(int: Int): Offset {
            require(int >= 0) { "Expected not negative number, but was $int" }
            return Offset(int)
        }
        public fun parseOrNull(int: Int): Offset? {
            if (int < 0) return null
            return Offset(int)
        }
    }
}

public val Int.offset: Offset get() = Offset.parse(int = this)
