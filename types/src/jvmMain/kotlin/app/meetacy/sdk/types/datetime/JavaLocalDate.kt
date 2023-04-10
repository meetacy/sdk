@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.types.datetime

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

public val LocalDate.meetacyDate: Date get() = Date(iso8601 = "$this")

public val Date.javaLocalDate: LocalDate get() = LocalDate.parse(iso8601)

public fun LocalDateTime.meetacyDateTime(zoneId: ZoneId): DateTime =
    atZone(zoneId).toInstant().meetacyDateTime

public fun DateTime.javaLocalDateTime(zoneId: ZoneId): LocalDateTime =
    javaInstant.atZone(zoneId).toLocalDateTime()
