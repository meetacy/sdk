package app.meetacy.sdk.io

import platform.Foundation.NSError

internal fun NSError?.toException(message: String? = null): Exception {
    if (this == null) return NullPointerException()
    return "code: ${this.code}, ${this.description}".let {  currentMessage ->
        if (message != null) {
            "$currentMessage, message: $message"
        } else {
            currentMessage
        }
    }.let { IllegalStateException(it) }
}
