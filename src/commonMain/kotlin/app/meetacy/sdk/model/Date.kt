package app.meetacy.sdk.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Date(val iso8601: String)
