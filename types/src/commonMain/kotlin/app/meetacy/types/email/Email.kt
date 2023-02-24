@file:Suppress("MemberVisibilityCanBePrivate")

package app.meetacy.types.email

import app.meetacy.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Email @UnsafeConstructor constructor(public val string: String) {
    @OptIn(UnsafeConstructor::class)
    public companion object {
        public val REGEX: Regex = Regex(".+@.+\\.+")

        public fun parse(string: String): Email {
            require(string.matches(REGEX)) { "String '$string' doesn't match email regex" }
            return Email(string)
        }
        public fun parseOrNull(string: String): Email? {
            if (!string.matches(REGEX)) return null
            return Email(string)
        }
    }
}
