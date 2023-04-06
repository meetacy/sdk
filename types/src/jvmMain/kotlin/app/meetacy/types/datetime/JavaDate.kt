@file:OptIn(UnsafeConstructor::class)

package app.meetacy.types.datetime

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.Date
import java.util.Date as JavaDate

public val JavaDate.meetacyDate: Date
    get() = Date(iso8601DateFormat.format(this))

public val JavaDate.meetacyDateTime: DateTime
    get() = DateTime(iso8601DateTimeFormat.format(this))

public val DateTime.javaDate: JavaDate get() =
    iso8601DateTimeFormat.parse(iso8601)
