package app.meetacy.sdk.types.url

import kotlin.jvm.JvmInline

@JvmInline
public value class Url(public val string: String) {

    public operator fun div(url: Url): Url {
        return if (string.endsWith(suffix = "/")) {
            Url(string = string + url)
        } else {
            Url(string = "$string/$url")
        }
    }

    public operator fun div(string: String): Url = div(Url(string))

    public operator fun plus(parameters: Parameters): Url {
        val string = buildString {
            append(string)

            for ((key, value) in parameters.map.entries) {
                if (string.contains('?')) {
                    append("&")
                } else {
                    append("?")
                }
                append(encodeQueryParam(key))
                append("=")
                append(encodeQueryParam(value))
            }
        }
        return Url(string)
    }
}

private fun encodeQueryParam(value: String): String = buildString {
    val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~"
    val utf8Bytes = value.encodeToByteArray()

    for (byte in utf8Bytes) {
        val char = byte.toInt().toChar()
        if (char in allowedChars) {
            append(char)
        } else {
            append('%')
            append(
                byte.toUByte()
                    .toString(radix = 16)
                    .padStart(length = 2, padChar = '0')
            )
        }
    }
}
