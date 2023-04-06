package app.meetacy.sdk.types.datetime

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.timeZoneForSecondsFromGMT
import platform.Foundation.NSDate as IosDate

internal val multiplatformDateFormatter: NSDateFormatter by lazy {
    NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"
        locale = NSLocale(localeIdentifier = "en_US_POSIX")
        timeZone = NSTimeZone.Companion.timeZoneForSecondsFromGMT(0)
    }
}

private val formats: List<String> get() = listOf(
    "yyyy-MM-dd'T'HH:mm:ssZZZZZ",
    "yyyy-MM-dd'T'HH:mm'Z'",
    "yyyy-MM-dd",
    "yyyy-MM-dd'T'HH:mm:ss'Z'"
)

internal fun NSDateFormatter.Companion.isoStringFromDate(date: IosDate): String =
    multiplatformDateFormatter.apply {
        dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"
    }.stringFromDate(date)

internal fun NSDateFormatter.Companion.dateFromIsoString(string: String): IosDate? {
    formats.forEach {
        val result = multiplatformDateFormatter
            .apply { dateFormat = it }
            .dateFromString(string)
        if (result != null) return result
    }
    return null
}
