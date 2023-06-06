package app.meetacy.sdk.io

import org.w3c.files.Blob
import kotlin.js.Promise

internal external interface Reader {
    fun read(): Promise<Result>
    fun cancel(): Promise<Unit>

    interface Result {
        val value: ByteArray?
        val done: Boolean
    }
}

internal fun Blob.reader(): Reader {
    val readerDynamic = asDynamic()
        .stream()
        .getReader()

    return readerDynamic.unsafeCast<Reader>()
}
