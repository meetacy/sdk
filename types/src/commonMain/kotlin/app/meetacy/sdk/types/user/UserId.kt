package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class UserId @UnsafeConstructor constructor(public val string: String)
