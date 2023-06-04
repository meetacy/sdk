package app.meetacy.sdk.files

import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create

public val NSURL.fileSize: ULong? get() = NSData.create(this)?.length
