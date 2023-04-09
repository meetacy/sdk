package app.meetacy.sdk.types.datetime

import kotlin.js.Date as JsDate

private val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""")
private val dateTimeRegex = Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}}Z""")

internal actual fun checkDate(iso8601: String): CheckDateResult =
    when {
        JsDate.parse(iso8601).isNaN() -> CheckDateResult.NotISO // Not even a date
        iso8601.matches(dateRegex) -> CheckDateResult.Success // Date & Matches Date-only Regex
        iso8601.matches(dateTimeRegex) -> CheckDateResult.ContainsTime // Date, but also contains time
        else -> CheckDateResult.NotISO // Date, but not in ISO
    }

internal actual fun checkDateTime(iso8601: String): CheckDateTimeResult =
    when {
        JsDate.parse(iso8601).isNaN() -> CheckDateTimeResult.NotISO // Not even a date
        iso8601.matches(dateTimeRegex) -> CheckDateTimeResult.Success // Date & Matches DateTime regex
        iso8601.matches(dateRegex) -> CheckDateTimeResult.HasNoTime // Date, but does not contain time
        else -> CheckDateTimeResult.NotISO // Date, but not in ISO
    }

internal actual fun DateTime.extractDate(): Date = jsDate.meetacyDate

internal actual fun todayDate(): Date = JsDate().meetacyDate

internal actual fun nowDateTime(): DateTime = JsDate().meetacyDateTime

internal actual fun Date.extractAtStartOfDay(): DateTime = JsDate(iso8601).meetacyDateTime
