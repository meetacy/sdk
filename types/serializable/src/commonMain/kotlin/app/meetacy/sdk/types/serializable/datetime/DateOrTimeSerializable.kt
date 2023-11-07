package app.meetacy.sdk.types.serializable.datetime

import app.meetacy.sdk.types.datetime.DateOrTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class DateOrTimeSerializable(public val string: String)

public fun DateOrTimeSerializable.type(): DateOrTime = DateOrTime.parse(string)
public fun DateOrTime.serializable(): DateOrTimeSerializable = DateOrTimeSerializable(iso8601)
