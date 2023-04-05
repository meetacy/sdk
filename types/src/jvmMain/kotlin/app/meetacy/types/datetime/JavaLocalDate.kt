@file:OptIn(UnsafeConstructor::class)

package app.meetacy.types.datetime

import app.meetacy.types.annotation.UnsafeConstructor
import java.time.*
import java.util.Date as JavaDate

public val LocalDate.meetacyDate: Date get() = Date(iso8601 = "$this")

public val Date.javaLocalDate: LocalDate get() = LocalDate.parse(iso8601)

public val LocalDateTime.meetacyDateTime: DateTime
    get() = DateTime(iso8601DateTimeFormat.format(atZone(ZoneId.systemDefault())))

public val DateTime.javaLocalDateTime: LocalDateTime get() =
    iso8601DateTimeFormat
        .parse(iso8601)
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
