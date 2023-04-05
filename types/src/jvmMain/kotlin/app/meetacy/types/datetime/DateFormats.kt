package app.meetacy.types.datetime

import java.text.SimpleDateFormat
import java.util.*

internal val iso8601DateFormat = SimpleDateFormat("yyyy-MM-dd").apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

internal val iso8601DateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").apply {
    timeZone = TimeZone.getTimeZone("UTC")
}
