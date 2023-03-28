package app.meetacy.types.meeting

import app.meetacy.types.file.FileId
import app.meetacy.types.location.Location
import app.meetacy.types.user.User
import app.meetacy.types.datetime.Date as DateFromTypes
import app.meetacy.types.datetime.DateTime as DateTimeFromTypes

/**
 * !!! Warning !!!
 *
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.api.meetings.MeetingRepository]
 * - [app.meetacy.api.meetings.AuthorizedMeetingRepository]
 */
public data class Meeting(
    val id: MeetingId,
    val creator: User,
    val date: DateTimeInfo,
    val location: Location,
    val title: String,
    val description: String?,
    val participantsCount: Int,
    val isParticipating: Boolean,
    val avatarIdentity: FileId?
) {
    public sealed interface DateTimeInfo {
        public val date: DateFromTypes
        public val dateTime: DateTimeFromTypes?
        public val iso8601: String

        public class Date(override val date: DateFromTypes) : DateTimeInfo {
            override val dateTime: Nothing? get() = null
            override val iso8601: String get() = date.iso8601

            public companion object {
                public fun today(): Date = Date(DateFromTypes.today())
                public fun parse(iso8601: String): Date =
                    Date(DateFromTypes.parse(iso8601))
                public fun parseOrNull(iso8601: String): Date? =
                    DateFromTypes.parseOrNull(iso8601)?.let(::Date)
            }
        }
        public class DateTime(override val dateTime: DateTimeFromTypes) : DateTimeInfo {
            override val date: DateFromTypes get() = dateTime.date
            override val iso8601: String get() = dateTime.iso8601

            public companion object {
                public fun now(): DateTime = DateTime(DateTimeFromTypes.now())
                public fun parse(iso8601: String): DateTime =
                    DateTime(DateTimeFromTypes.parse(iso8601))
                public fun parseOrNull(iso8601: String): DateTime? =
                    DateTimeFromTypes.parseOrNull(iso8601)?.let(::DateTime)
            }
        }

        public companion object {
            public fun parse(iso8601: String): DateTimeInfo =
                parseOrNull(iso8601) ?: error("Given string '$iso8601' is not in iso8601 format")

            public fun parseOrNull(iso8601: String): DateTimeInfo? =
                DateTimeFromTypes.parseOrNull(iso8601)?.let(::DateTime) ?:
                DateFromTypes.parseOrNull(iso8601)?.let(::Date)
        }
    }
}
