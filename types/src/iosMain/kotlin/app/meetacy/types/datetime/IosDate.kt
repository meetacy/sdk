@file:OptIn(UnsafeConstructor::class)

package app.meetacy.types.datetime

import app.meetacy.sdk.types.datetime.DateTime
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.datetime.Date
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDate as IosDate

public val IosDate.meetacyDate: Date
    get() = Date(toISOString().take(10))

internal fun IosDate(iso8601: String): IosDate =
    NSDateFormatter.dateFromIsoString(iso8601)
        ?: error("NSDate represents is nil")

internal fun IosDate.toISOString(): String =
    NSDateFormatter.isoStringFromDate(this)

public val DateTime.iosDate: IosDate get() = IosDate(iso8601)

public val IosDate.meetacyDateTime: DateTime
    get() = DateTime(toISOString())
