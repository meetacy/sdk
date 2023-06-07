package app.meetacy.sdk.io

import platform.Foundation.NSError

public sealed interface IosResult<out T> {
    public class Success<T>(public val value: T) : IosResult<T>
    public class Failure(public val error: NSError) : IosResult<Nothing>

    public fun asKotlinResult(): Result<T> = when (this) {
        is Failure -> Result.failure(Exception(error))
        is Success -> Result.success(value)
    }

    public class Exception(
        public val error: NSError
    ) : RuntimeException(
        message = "Native Exception Occurred: ${error.localizedDescription}"
    )
}
