package app.meetacy.sdk.example

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.engine.darwin.DarwinLegacy
import platform.Foundation.NSError
import kotlin.native.concurrent.AtomicReference

object ThrowableToNSErrorMapper : (Throwable) -> NSError? {
    private val mapperRef: AtomicReference<((Throwable) -> NSError?)?> = AtomicReference(null)

    override fun invoke(throwable: Throwable): NSError? {
        @Suppress("MaxLineLength")
        return requireNotNull(mapperRef.value) {
            "please setup ThrowableToNSErrorMapper by call ThrowableToNSErrorMapper.setup() in iosMain"
        }.invoke(throwable)
    }

    fun setup(block: (Throwable) -> NSError?) {
        mapperRef.value = block
    }
}

internal actual fun createHttpClientEngine(): HttpClientEngine {
    // configure darwin throwable mapper
    ThrowableToNSErrorMapper.setup { (it as? DarwinHttpRequestException)?.origin }
    // configure darwin
    return DarwinLegacy.create {

    }
}
