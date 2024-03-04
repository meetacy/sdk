@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.types.serializable.amount

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class AmountSerializable(public val int: Int) {
    @Serializable
    @JvmInline
    public value class OrZero(public val int: Int)
}

public fun AmountSerializable.type(): Amount = Amount(int)
public fun Amount.serializable(): AmountSerializable = AmountSerializable(int)

public fun AmountSerializable.OrZero.type(): Amount.OrZero = Amount.OrZero(int)
public fun Amount.OrZero.serializable(): AmountSerializable.OrZero = AmountSerializable.OrZero(int)
