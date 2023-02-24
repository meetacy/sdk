package app.meetacy.types.amount

import app.meetacy.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Amount @UnsafeConstructor constructor(public val int: Int) {
    @OptIn(UnsafeConstructor::class)
    public companion object {
        public fun parse(int: Int): Amount {
            require(int > 0) { "Positive number expected, but was $int" }
            return Amount(int)
        }
        public fun parseOrNull(int: Int): Amount? {
            if (int <= 0) return null
            return Amount(int)
        }
    }
}

public val Int.amount: Amount get() = Amount.parse(int = this)
