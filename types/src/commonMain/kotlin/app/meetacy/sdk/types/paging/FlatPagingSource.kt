package app.meetacy.sdk.types.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public fun interface FlatPagingSource<T> {
    public operator fun iterator(): FlatPagingIterator<T>
}

public inline fun <T, R> FlatPagingSource<T>.map(
    crossinline transform: suspend (T) -> R
) : FlatPagingSource<R> {
    return FlatPagingSource {
        object : FlatPagingIterator<R> {
            private val base = this@map.iterator()
            override suspend fun hasNext() = base.hasNext()
            override suspend fun next(): R = transform(base.next())
        }
    }
}

public fun <T> FlatPagingSource<T>.asFlow(): Flow<T> = flow {
    for (element in this@asFlow) {
        emit(element)
    }
}
