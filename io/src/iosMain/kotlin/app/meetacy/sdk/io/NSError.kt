package app.meetacy.sdk.io

import kotlinx.cinterop.*
import platform.Foundation.NSError
import platform.posix.err

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

public inline fun <T> MemScope.runCatching(block: (ObjCObjectVar<NSError?>) -> T): IosResult<T> {
    val error = alloc<ObjCObjectVar<NSError?>>()
    val result = kotlin.runCatching { block(error) }

    val errorValue = error.value

    return if (errorValue == null) {
        IosResult.Success(result.getOrThrow())
    } else {
        IosResult.Failure(errorValue)
    }
}

public inline fun <T> runMemScopedCatching(block: (ObjCObjectVar<NSError?>) -> T): IosResult<T> {
    return memScoped {
        runCatching(block)
    }
}
