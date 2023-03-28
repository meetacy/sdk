package app.meetacy.types.datetime

import java.text.ParseException
import java.util.Date as JavaDate

/**
 * Check whether a given string is iso8601 and doesn't specify time
 */
internal actual fun checkDate(iso8601: String): CheckDateResult = try {
    iso8601DateFormat.parse(iso8601)
    CheckDateResult.Success
} catch (_: ParseException) {
    try {
        iso8601DateTimeFormat.parse(iso8601)
        CheckDateResult.ContainsTime
    } catch (_: ParseException) {
        CheckDateResult.NotISO
    }
}

internal actual fun checkDateTime(iso8601: String): CheckDateTimeResult = try {
    iso8601DateTimeFormat.parse(iso8601)
    CheckDateTimeResult.Success
} catch (_: ParseException) {
    try {
        iso8601DateFormat.parse(iso8601)
        CheckDateTimeResult.HasNoTime
    } catch (_: ParseException) {
        CheckDateTimeResult.NotISO
    }
}

internal actual fun DateTime.extractDate(): Date = javaLocalDateTime.meetacyDate

internal actual fun todayDate(): Date = JavaDate().meetacyDate

internal actual fun nowDateTime(): DateTime = JavaDate().meetacyDateTime
