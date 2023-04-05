package app.meetacy.types.datetime

import platform.Foundation.NSDate as IosDate

internal val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""")
internal val dateTimeRegex = Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}Z""")

internal actual fun checkDate(iso8601: String): CheckDateResult =
    when {
        multiplatformDateFormatter.dateFromString(iso8601) == null -> CheckDateResult.NotISO // Not even a date
        iso8601.matches(dateRegex) -> CheckDateResult.Success // Date & Matches Date-only Regex
        iso8601.matches(dateTimeRegex) -> CheckDateResult.ContainsTime // Date, but also contains time
        else -> CheckDateResult.NotISO // Date, but not in ISO
    }

internal actual fun checkDateTime(iso8601: String): CheckDateTimeResult =
    when {
        multiplatformDateFormatter.dateFromString(iso8601) == null -> CheckDateTimeResult.NotISO // Not even a date
        iso8601.matches(dateTimeRegex) -> CheckDateTimeResult.Success // Date & Matches DateTime regex
        iso8601.matches(dateRegex) -> CheckDateTimeResult.HasNoTime // Date, but does not contain time
        else -> CheckDateTimeResult.NotISO // Date, but not in ISO
    }

internal actual fun DateTime.extractDate(): Date = iosDate.meetacyDate

internal actual fun todayDate(): Date = IosDate().meetacyDate

internal actual fun nowDateTime(): DateTime = IosDate().meetacyDateTime

internal actual fun Date.extractAtStartOfDay(): DateTime = IosDate(iso8601).meetacyDateTime
