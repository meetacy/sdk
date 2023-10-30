package app.meetacy.sdk.types.serializable.datetime

import app.meetacy.sdk.types.datetime.DateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class DateTimeSerializable(public val iso8601: String)

public fun DateTimeSerializable.type(): DateTime = DateTime.parse(iso8601)
public fun DateTime.serializable(): DateTimeSerializable = DateTimeSerializable(iso8601)
