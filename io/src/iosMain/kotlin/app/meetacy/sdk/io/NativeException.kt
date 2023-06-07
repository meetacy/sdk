package app.meetacy.sdk.io

import platform.Foundation.NSError

public class NativeException(public val error: NSError) : Throwable()
