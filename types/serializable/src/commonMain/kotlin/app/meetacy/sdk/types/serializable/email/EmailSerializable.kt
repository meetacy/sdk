@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.types.serializable.email

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.email.Email
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class EmailSerializable(public val string: String)

public fun EmailSerializable.type(): Email = Email(string)
public fun Email.serializable(): EmailSerializable = EmailSerializable(string)
