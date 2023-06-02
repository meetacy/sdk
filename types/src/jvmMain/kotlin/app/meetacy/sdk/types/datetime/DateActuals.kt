package app.meetacy.sdk.types.datetime

import java.text.ParseException
import java.time.Instant
import java.time.LocalDate
import java.util.Date as JavaDate

private val dateTimeRegex = Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z""")

/**
 * Check whether a given string is iso8601 and doesn't specify time
 */
internal actual fun checkDate(iso8601: String): CheckDateResult = try {
    LocalDate.parse(iso8601)
    CheckDateResult.Success
} catch (_: Throwable) {
    try {
        Instant.parse(iso8601)
        iso8601.matches(dateTimeRegex) || throw IllegalStateException()
        CheckDateResult.ContainsTime
    } catch (_: ParseException) {
        CheckDateResult.NotISO
    }
}

internal actual fun checkDateTime(iso8601: String): CheckDateTimeResult = try {
    Instant.parse(iso8601)
    if (!iso8601.matches(dateTimeRegex)) throw IllegalStateException()
    CheckDateTimeResult.Success
} catch (_: ParseException) {
    try {
        LocalDate.parse(iso8601)
        CheckDateTimeResult.HasNoTime
    } catch (_: ParseException) {
        CheckDateTimeResult.NotISO
    }
}

internal actual fun DateTime.extractDate(): Date = javaDate.meetacyDate

internal actual fun todayDate(): Date = JavaDate().meetacyDate

internal actual fun nowDateTime(): DateTime = JavaDate().meetacyDateTime

internal actual fun Date.extractAtStartOfDay(): DateTime = iso8601DateFormat.parse(iso8601).meetacyDateTime
