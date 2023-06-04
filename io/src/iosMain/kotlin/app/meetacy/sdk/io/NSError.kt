package app.meetacy.sdk.io

import kotlinx.cinterop.*
import platform.Foundation.NSError

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
