package app.meetacy.sdk.types.datetime

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlin.jvm.JvmInline

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.types.datetime.DateOrTime.DateTime]
 */
@JvmInline
public value class DateTime @UnsafeConstructor constructor(public val iso8601: String) {
    public val date: Date get() = extractDate()

    @OptIn(UnsafeConstructor::class)
    public companion object {
        public fun now(): DateTime = nowDateTime()

        public fun parse(iso8601: String): DateTime = when (checkDateTime(iso8601)) {
            CheckDateTimeResult.HasNoTime ->
                error("Given string '$iso8601' doesn't contain any time, consider to use 'Date' class instead.")
            CheckDateTimeResult.NotISO ->
                error("Given string '$iso8601' is not in iso8601 format (yyyy-mm-ddThh:MM:ssZ, e.g. 2000-01-31T12:00:00Z)")
            CheckDateTimeResult.Success -> DateTime(iso8601)
        }

        public fun parseOrNull(iso8601: String): DateTime? =
            if (checkDateTime(iso8601) is CheckDateTimeResult.Success) {
                DateTime(iso8601)
            } else {
                null
            }
    }
}

internal sealed interface CheckDateTimeResult {
    data object Success : CheckDateTimeResult
    data object NotISO : CheckDateTimeResult
    data object HasNoTime : CheckDateTimeResult
}

internal expect fun checkDateTime(iso8601: String): CheckDateTimeResult

internal expect fun DateTime.extractDate(): Date

internal expect fun nowDateTime(): DateTime
