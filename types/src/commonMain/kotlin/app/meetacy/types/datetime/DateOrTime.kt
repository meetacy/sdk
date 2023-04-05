package app.meetacy.types.datetime

import app.meetacy.types.datetime.Date as DateType
import app.meetacy.types.datetime.DateTime as DateTimeType

public sealed interface DateOrTime {
    public val date: DateType
    public val dateTime: DateTimeType?
    public val iso8601: String

    public class Date(override val date: DateType) : DateOrTime {
        override val dateTime: Nothing? get() = null
        override val iso8601: String get() = date.iso8601

        public companion object {
            public fun today(): Date = Date(DateType.today())
            public fun parse(iso8601: String): Date =
                Date(DateType.parse(iso8601))
            public fun parseOrNull(iso8601: String): Date? =
                DateType.parseOrNull(iso8601)?.let(DateOrTime::Date)
        }
    }

    public class DateTime(override val dateTime: DateTimeType) : DateOrTime {
        override val date: DateType get() = dateTime.date
        override val iso8601: String get() = dateTime.iso8601

        public companion object {
            public fun now(): DateTime = DateTime(DateTimeType.now())
            public fun parse(iso8601: String): DateTime =
                DateTime(DateTimeType.parse(iso8601))
            public fun parseOrNull(iso8601: String): DateTime? =
                DateTimeType.parseOrNull(iso8601)?.let(DateOrTime::DateTime)
        }
    }

    public companion object {
        public fun parse(iso8601: String): DateOrTime =
            parseOrNull(iso8601) ?: error("Given string '$iso8601' is not in iso8601 format")

        public fun parseOrNull(iso8601: String): DateOrTime? =
            DateTimeType.parseOrNull(iso8601)?.let(DateOrTime::DateTime) ?:
            DateType.parseOrNull(iso8601)?.let(DateOrTime::Date)
    }
}
