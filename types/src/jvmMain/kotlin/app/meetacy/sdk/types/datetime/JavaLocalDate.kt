@file:OptIn(UnsafeConstructor::class)

package app.meetacy.sdk.types.datetime

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.Date
import java.time.*

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
