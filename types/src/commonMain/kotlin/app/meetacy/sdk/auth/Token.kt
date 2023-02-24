package app.meetacy.sdk.auth

import app.meetacy.sdk.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Token @UnsafeConstructor constructor(public val string: String)
