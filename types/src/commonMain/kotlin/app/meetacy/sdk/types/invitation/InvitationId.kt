package app.meetacy.sdk.types.invitation

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class InvitationId @UnsafeConstructor constructor(public val string: String)
