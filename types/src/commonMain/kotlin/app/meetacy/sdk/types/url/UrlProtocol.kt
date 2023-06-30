package app.meetacy.sdk.types.url

import app.meetacy.sdk.types.annotation.UnstableApi
import kotlin.jvm.JvmInline

@UnstableApi
@JvmInline
public value class UrlProtocol(public val string: String) {
    public val isHttp: Boolean get() = string == "http"
    public val isHttps: Boolean get() = string == "https"
    public val isWs: Boolean get() = string == "ws"
    public val isWss: Boolean get() = string == "wss"

    public fun toWebSocket(): UrlProtocol = when {
        isHttp -> Ws
        isHttps -> Wss
        else -> error("Cannot convert url to websocket protocol")
    }

    public companion object {
        public val Http: UrlProtocol = UrlProtocol(string = "http")
        public val Https: UrlProtocol = UrlProtocol(string = "https")
        public val Ws: UrlProtocol = UrlProtocol(string = "ws")
        public val Wss: UrlProtocol = UrlProtocol(string = "wss")
    }
}
