package app.meetacy.types.state

import kotlin.jvm.JvmInline

/**
 * Represents identifier of entity state. Used to properly retrieve updates.
 */
@JvmInline
public value class StateId(public val string: String)
