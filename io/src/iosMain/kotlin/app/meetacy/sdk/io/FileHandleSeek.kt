package app.meetacy.sdk.io

import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError
import platform.Foundation.NSFileHandle
import platform.Foundation.seekToEndOfFile

internal fun NSFileHandle.seek(offset: ULong): Pair<Boolean?, NSError?> {
    return memScoped {
        val error = alloc<ObjCObjectVar<NSError?>>()
        val result: Boolean? = runCatching {
            seekToOffset(offset, error.ptr)
        }.getOrNull()
        result to error.value
    }
}

internal fun NSFileHandle.getOffset(): Pair<ULong?, NSError?> {
    return memScoped {
        val error = alloc<ObjCObjectVar<NSError?>>()
        val offset = alloc<ULongVar>()
        runCatching {
            getOffset(offset.ptr, error.ptr)
        }.getOrNull()
        offset.value to error.value
    }
}

public val NSFileHandle.size: ULong get() = seekToEndOfFile().also { _ ->
    this.seek(0u)
}
