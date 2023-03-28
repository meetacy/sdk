package app.meetacy.types.datetime

import app.meetacy.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.types.meeting.Meeting.DateTimeInfo.Date]
 */
@JvmInline
public value class Date @UnsafeConstructor constructor(public val iso8601: String) {
    @OptIn(UnsafeConstructor::class)
    public companion object {
        public fun today(): Date = todayDate()

        public fun parse(iso8601: String): Date = when (checkDate(iso8601)) {
            CheckDateResult.ContainsTime ->
                error("Given string '$iso8601' contains time, consider to use 'DateTime' class instead")
            CheckDateResult.NotISO ->
                error("Given string '$iso8601' is not in iso8601 format")
            CheckDateResult.Success -> Date(iso8601)
        }

        public fun parseOrNull(iso8601: String): Date? =
            if (checkDate(iso8601) is CheckDateResult.Success) {
                Date(iso8601)
            } else {
                null
            }
    }
}

internal sealed interface CheckDateResult {
    data object Success : CheckDateResult
    data object NotISO : CheckDateResult
    data object ContainsTime : CheckDateResult
}

/**
 * Check whether a given string is iso8601 and doesn't specify time
 */
internal expect fun checkDate(iso8601: String): CheckDateResult

internal expect fun todayDate(): Date
