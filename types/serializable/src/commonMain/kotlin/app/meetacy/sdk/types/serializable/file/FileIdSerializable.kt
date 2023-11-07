package app.meetacy.sdk.types.serializable.file

import app.meetacy.sdk.types.file.FileId
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class FileIdSerializable(public val string: String)

public fun FileIdSerializable.type(): FileId = FileId(string)
public fun FileId.serializable(): FileIdSerializable = FileIdSerializable(string)
