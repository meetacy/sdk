package app.meetacy.types.update

import kotlin.jvm.JvmInline

/**
 * Represents identifier of entity state. Used to properly retrieve updates.
 */
@JvmInline
public value class UpdateId(public val string: String)
