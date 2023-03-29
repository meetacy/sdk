@file:OptIn(UnsafeConstructor::class)

package app.meetacy.types.datetime

import app.meetacy.types.annotation.UnsafeConstructor
import kotlin.js.Date as JsDate

public val JsDate.meetacyDate: Date
    get() {
        require(!getTime().isNaN()) { "Js Date represents NaN, thus cannot be converted to Meetacy Date" }
        return Date(toISOString().take(10))
    }

public val Date.jsDate: JsDate get() = JsDate(iso8601)


public val JsDate.meetacyDateTime: DateTime
    get() {
        require(!getTime().isNaN()) { "Js Date represents NaN, thus cannot be converted to Meetacy DateTime" }
        return DateTime(toISOString())
    }

public val DateTime.jsDate: JsDate get() = JsDate(iso8601)
