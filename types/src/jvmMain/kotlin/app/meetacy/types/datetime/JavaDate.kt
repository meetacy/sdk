@file:OptIn(UnsafeConstructor::class)

package app.meetacy.types.datetime

import app.meetacy.types.annotation.UnsafeConstructor
import java.util.Date as JavaDate

public val JavaDate.meetacyDate: Date
    get() = Date(iso8601DateFormat.format(this))

public val Date.javaDate: JavaDate get() =
    iso8601DateFormat.parse(iso8601)

public val JavaDate.meetacyDateTime: DateTime
    get() = DateTime(iso8601DateTimeFormat.format(this))

public val DateTime.javaDate: JavaDate get() =
    iso8601DateTimeFormat.parse(iso8601)
