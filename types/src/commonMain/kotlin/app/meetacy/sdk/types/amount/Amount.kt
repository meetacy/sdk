package app.meetacy.sdk.types.amount

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Amount @UnsafeConstructor constructor(public val int: Int) {
    public val orZero: OrZero get() = OrZero.parse(int)

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

    public data class OrZero @UnsafeConstructor constructor(public val int: Int) {
        public fun toNonZero(): Amount = Amount.parse(int)
        public fun toNonZeroOrNull(): Amount? = Amount.parseOrNull(int)

        @OptIn(UnsafeConstructor::class)
        public companion object {
            public fun parse(int: Int): OrZero {
                require(int >= 0) { "Non negative number expected, but was $int" }
                return OrZero(int)
            }
            public fun parseOrNull(int: Int): OrZero? {
                if (int >= 0) return null
                return OrZero(int)
            }
        }
    }
}

public val Int.amount: Amount get() = Amount.parse(int = this)
public val Int.amountOrZero: Amount.OrZero get() = Amount.OrZero.parse(int = this)
