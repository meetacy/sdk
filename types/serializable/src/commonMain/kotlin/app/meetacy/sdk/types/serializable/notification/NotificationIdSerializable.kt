package app.meetacy.sdk.types.serializable.notification

import app.meetacy.sdk.types.notification.NotificationId
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline


@Serializable
@JvmInline
public value class NotificationIdSerializable(public val string: String)
public fun NotificationIdSerializable.type(): NotificationId = NotificationId(string)
public fun NotificationId.serializable(): NotificationIdSerializable = NotificationIdSerializable(string)