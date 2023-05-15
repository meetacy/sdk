package app.meetacy.sdk.types.paging

import app.meetacy.sdk.types.amount.Amount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public fun <T> PagingSource(
    chunkSize: Amount,
    startPagingId: PagingId? = null,
    limit: Amount? = null,
    provider: suspend (amount: Amount, pagingId: PagingId?) -> PagingResponse<T>
): PagingSource<T> = PagingSource {
    _PagingIterator(chunkSize, startPagingId, limit, provider)
}

public fun interface PagingSource<T> {
    public operator fun iterator(): PagingIterator<T>
}

public inline fun <T, R> PagingSource<T>.map(
    crossinline transform: suspend (List<T>) -> List<R>
): PagingSource<R> {
    return PagingSource {
        object : PagingIterator<R> {
            private val base = this@map.iterator()
            override suspend fun hasNext(): Boolean = base.hasNext()
            override suspend fun next(): List<R> = transform(base.next())
        }
    }
}

public inline fun <T, R> PagingSource<T>.mapItems(
    crossinline transform: suspend (T) -> R
): PagingSource<R> = map { data ->
    data.map { item ->
        transform(item)
    }
}

public fun <T> PagingSource<T>.flatten(): FlatPagingSource<T> {
    return FlatPagingSource { _FlatPagingIterator(base = this@flatten.iterator()) }
}

public fun <T> PagingSource<T>.asFlow(): Flow<List<T>> = flow {
    for (element in this@asFlow) {
        emit(element)
    }
}
