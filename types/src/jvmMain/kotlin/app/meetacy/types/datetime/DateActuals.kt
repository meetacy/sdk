package app.meetacy.types.datetime

import app.meetacy.sdk.types.datetime.CheckDateResult
import app.meetacy.sdk.types.datetime.CheckDateTimeResult
import app.meetacy.sdk.types.datetime.Date
import app.meetacy.sdk.types.datetime.DateTime
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

internal actual fun DateTime.extractDate(): Date = javaDate.meetacyDate

internal actual fun todayDate(): Date = JavaDate().meetacyDate

internal actual fun nowDateTime(): DateTime = JavaDate().meetacyDateTime

internal actual fun Date.extractAtStartOfDay(): DateTime = iso8601DateFormat.parse(iso8601).meetacyDateTime
