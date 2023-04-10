package app.meetacy.sdk.types.datetime

import app.meetacy.sdk.types.annotation.UnsafeConstructor
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

public val Instant.meetacyDate: Date get() = meetacyDateTime.date

@OptIn(UnsafeConstructor::class)
public val LocalDate.meetacyDate: Date get() = Date(toString())
