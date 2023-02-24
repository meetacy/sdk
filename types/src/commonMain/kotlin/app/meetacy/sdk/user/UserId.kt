package app.meetacy.sdk.user

import app.meetacy.sdk.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class UserId @UnsafeConstructor constructor(public val string: String)
