@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.types.serializable.user

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.user.UserId
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class UserIdSerializable(public val string: String)

public fun UserIdSerializable.type(): UserId = UserId(string)
public fun UserId.serializable(): UserIdSerializable = UserIdSerializable(string)
