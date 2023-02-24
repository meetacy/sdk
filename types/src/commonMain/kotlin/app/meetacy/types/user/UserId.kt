package app.meetacy.types.user

import app.meetacy.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class UserId @UnsafeConstructor constructor(public val string: String)
