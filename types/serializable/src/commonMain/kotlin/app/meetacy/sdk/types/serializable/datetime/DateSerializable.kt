package app.meetacy.sdk.types.serializable.datetime

import app.meetacy.sdk.types.datetime.Date
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class DateSerializable(public val iso8601: String)

public fun DateSerializable.type(): Date = Date.parse(iso8601)
public fun Date.serializable(): DateSerializable = DateSerializable(iso8601)
