package app.meetacy.sdk.types.user

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

@JvmInline
public value class Username @UnsafeConstructor constructor(public val string: String) {

    @OptIn(UnsafeConstructor::class)
    public companion object {
        public fun parse(string: String): Username {
            require(checkUsername(string)) { "Username doesn't match the following pattern: [a-zA-Z][a-zA-Z0-9_]*" }
            return Username(string)
        }
        public fun parseOrNull(string: String): Username? {
            if (!checkUsername(string)) return null
            return Username(string)
        }
    }
}

public val String.username: Username get() = Username.parse(string = this)
public val String.usernameOrNull: Username? get() = Username.parseOrNull(string = this)

private fun checkUsername(username: String): Boolean {
    val regex = Regex("[a-zA-Z][a-zA-Z0-9_]*")
    return regex.matches(username)
}
