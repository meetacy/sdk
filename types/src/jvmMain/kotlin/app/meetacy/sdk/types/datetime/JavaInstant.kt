package app.meetacy.sdk.types.datetime

import java.time.Instant
import java.util.Date as JavaDate

public val Instant.meetacyDate: Date get() = JavaDate(toEpochMilli()).meetacyDate

public val Instant.meetacyDateTime: DateTime get() = JavaDate(toEpochMilli()).meetacyDateTime

public val DateTime.javaInstant: Instant get() = Instant.parse(iso8601)
