package app.meetacy.sdk.types.optional

public sealed interface Optional<out T> {
    public val value: T? get() = null

    public data class Present<T>(override val value: T) : Optional<T>
    public data object Undefined : Optional<Nothing>
}

public inline fun <T, R> Optional<T>.map(transform: (T) -> R): Optional<R> =
    when (this) {
        is Optional.Present -> Optional.Present(transform(value))
        is Optional.Undefined -> Optional.Undefined
    }

public inline fun <T, R> Optional<T>.ifPresent(block: (T) -> R): R? = when (this) {
    is Optional.Present -> block(value)
    is Optional.Undefined -> null
}

public inline fun <T, R> Optional<T>.ifUndefined(block: () -> R): R? = when (this) {
    is Optional.Present -> null
    is Optional.Undefined -> block()
}
