package app.meetacy.sdk.types.datetime

import kotlinx.datetime.*

public val DateTime.kotlinxInstant: Instant get() = Instant.parse(iso8601)

public val Instant.meetacyDateTime: DateTime get() = DateTime.parse(toString())

public fun DateTime.kotlinxLocalDateTime(timeZone: TimeZone): LocalDateTime =
    kotlinxInstant.toLocalDateTime(timeZone)

public fun LocalDateTime.meetacyDateTime(timeZone: TimeZone): DateTime =
    toInstant(timeZone).meetacyDateTime
