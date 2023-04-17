package app.meetacy.sdk.version

import kotlin.jvm.JvmInline

@JvmInline
public value class ApiVersion(public val int: Int) {
    public companion object {
        // First versioning was implemented
        public val VersioningFeature: ApiVersion = ApiVersion(0)

        public fun latest(): ApiVersion = VersioningFeature
    }
}
