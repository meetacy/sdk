package app.meetacy.sdk.types.serializable.paging

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.paging.PagingId
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class PagingIdSerializable(public val string: String)

@OptIn(UnsafeConstructor::class)
public fun PagingIdSerializable.type(): PagingId = PagingId(string)
public fun PagingId.serializable(): PagingIdSerializable = PagingIdSerializable(string)
