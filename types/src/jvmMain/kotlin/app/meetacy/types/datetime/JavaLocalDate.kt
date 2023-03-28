@file:OptIn(UnsafeConstructor::class)

package app.meetacy.types.datetime

import app.meetacy.types.annotation.UnsafeConstructor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

public val LocalDate.meetacyDate: Date get() = atStartOfDay().meetacyDate

public val LocalDateTime.meetacyDate: Date
    get() = Date(iso8601DateFormat.format(atZone(ZoneId.systemDefault())))

public val Date.javaLocalDate: LocalDate get() =
    iso8601DateFormat
        .parse(iso8601)
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

public val LocalDateTime.meetacyDateTime: DateTime
    get() = DateTime(iso8601DateTimeFormat.format(atZone(ZoneId.systemDefault())))

public val DateTime.javaLocalDateTime: LocalDateTime get() =
    iso8601DateTimeFormat
        .parse(iso8601)
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
