package app.meetacy.sdk.types.auth

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Token @UnsafeConstructor constructor(public val string: String)
