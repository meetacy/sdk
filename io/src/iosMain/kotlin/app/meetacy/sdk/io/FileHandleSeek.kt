package app.meetacy.sdk.io

import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError
import platform.Foundation.NSFileHandle

internal fun NSFileHandle.seek(offset: Int): Pair<Boolean?, NSError?> {
    return memScoped {
        val error = alloc<ObjCObjectVar<NSError?>>()
        val result: Boolean? = runCatching {
            seekToOffset(offset.toULong(), error.ptr)
        }.getOrNull()
        result to error.value
    }
}
