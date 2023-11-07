@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.types.serializable.user

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.user.Username
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class UsernameSerializable(public val string: String)

public fun UsernameSerializable.type(): Username = Username(string)
public fun Username.serializable(): UsernameSerializable = UsernameSerializable(string)
