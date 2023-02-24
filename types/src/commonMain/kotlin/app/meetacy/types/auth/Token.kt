package app.meetacy.types.auth

import app.meetacy.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Token @UnsafeConstructor constructor(public val string: String)
