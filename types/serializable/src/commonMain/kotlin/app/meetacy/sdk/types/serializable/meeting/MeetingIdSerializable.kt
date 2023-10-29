package app.meetacy.sdk.types.serializable.meeting

import app.meetacy.sdk.types.meeting.MeetingId
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class MeetingIdSerializable(public val string: String)

public fun MeetingIdSerializable.type(): MeetingId = MeetingId(string)
public fun MeetingId.serializable(): MeetingIdSerializable = MeetingIdSerializable(string)