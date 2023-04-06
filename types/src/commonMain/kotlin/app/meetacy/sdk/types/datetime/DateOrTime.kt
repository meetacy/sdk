package app.meetacy.sdk.types.datetime

import app.meetacy.sdk.types.datetime.Date as DateType
import app.meetacy.sdk.types.datetime.DateTime as DateTimeType

public sealed interface DateOrTime {
    public val date: DateType
    public val dateTime: DateTimeType?
    public val iso8601: String

    public class Date(override val date: DateType) : DateOrTime {
        override val dateTime: Nothing? get() = null
        override val iso8601: String get() = date.iso8601

        public companion object {
            public fun today(): Date = Date(app.meetacy.sdk.types.datetime.Date.today())
            public fun parse(iso8601: String): Date =
                Date(app.meetacy.sdk.types.datetime.Date.parse(iso8601))
            public fun parseOrNull(iso8601: String): Date? =
                app.meetacy.sdk.types.datetime.Date.parseOrNull(iso8601)?.let(DateOrTime::Date)
        }
    }

    public class DateTime(override val dateTime: DateTimeType) : DateOrTime {
        override val date: DateType get() = dateTime.date
        override val iso8601: String get() = dateTime.iso8601

        public companion object {
            public fun now(): DateTime = DateTime(app.meetacy.sdk.types.datetime.DateTime.now())
            public fun parse(iso8601: String): DateTime =
                DateTime(app.meetacy.sdk.types.datetime.DateTime.parse(iso8601))
            public fun parseOrNull(iso8601: String): DateTime? =
                app.meetacy.sdk.types.datetime.DateTime.parseOrNull(iso8601)?.let(DateOrTime::DateTime)
        }
    }

    public companion object {
        public fun parse(iso8601: String): DateOrTime =
            parseOrNull(iso8601) ?: error("Given string '$iso8601' is not in iso8601 format")

        public fun parseOrNull(iso8601: String): DateOrTime? =
            app.meetacy.sdk.types.datetime.DateTime.parseOrNull(iso8601)?.let(DateOrTime::DateTime) ?:
            app.meetacy.sdk.types.datetime.Date.parseOrNull(iso8601)?.let(DateOrTime::Date)
    }
}
