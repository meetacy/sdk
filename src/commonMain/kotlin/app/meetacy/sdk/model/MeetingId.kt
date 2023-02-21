package app.meetacy.sdk.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class MeetingId(val string: String)
