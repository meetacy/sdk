package app.meetacy.sdk.types.paging

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.amount.amount

public suspend fun <T> PagingRepository(
    amount: Amount,
    startPagingId: PagingId? = null,
    provider: suspend (amount: Amount, pagingId: PagingId?) -> PagingResponse<T>
): PagingRepository<T> {
    return PagingRepository(
        response = provider(amount, startPagingId),
        provider = provider
    )
}

public fun <T> PagingRepository(
    response: PagingResponse<T>,
    provider: suspend (amount: Amount, pagingId: PagingId) -> PagingResponse<T>
): PagingRepository<T> = object : PagingRepository<T> {
    override val response = response

    override suspend fun next(amount: Amount): PagingRepository<T> {
        response.nextPagingId ?: throw NoSuchElementException()

        val newResponse = provider(
            amount,
            response.nextPagingId
        )

        return PagingRepository(newResponse, provider)
    }
}

public interface PagingRepository<out T> {
    public val response: PagingResponse<T>

    public suspend fun next(amount: Amount = response.data.size.amount): PagingRepository<T>
}

public val <T> PagingRepository<T>.data: List<T> get() = response.data

public fun PagingRepository<*>.hasNext(): Boolean = response.nextPagingId != null

public operator fun <T> PagingRepository<T>.component1(): List<T> = data

public suspend fun <T> PagingRepository<T>.nextOrNull(): PagingRepository<T>? {
    if (!hasNext()) return null
    return next()
}

public fun <T, R> PagingRepository<T>.map(
    transform: (List<T>) -> List<R>
): PagingRepository<R> = object : PagingRepository<R> {
    override val response: PagingResponse<R> = this@map.response.map(transform)

    override suspend fun next(amount: Amount): PagingRepository<R> {
        return this@map.next(amount).map(transform)
    }
}

public suspend fun <T, R> PagingRepository<T>.mapSuspend(
    transform: suspend (List<T>) -> List<R>
): PagingRepository<R> {
    val response = response.map { transform(it) }

    return object : PagingRepository<R> {
        override val response = response

        override suspend fun next(amount: Amount): PagingRepository<R> {
            return this@mapSuspend.next(amount).mapSuspend(transform)
        }
    }
}

public fun <T, R> PagingRepository<T>.mapItems(
    transform: (T) -> R
): PagingRepository<R> = object : PagingRepository<R> {
    override val response = this@mapItems.response.mapItems(transform)

    override suspend fun next(amount: Amount): PagingRepository<R> {
        return this@mapItems.next(amount).mapItems(transform)
    }
}

public suspend fun <T, R> PagingRepository<T>.mapItemsSuspend(
    transform: suspend (T) -> R
): PagingRepository<R> {
    val response = response.mapItems { transform(it) }

    return object : PagingRepository<R> {
        override val response = response
        override suspend fun next(amount: Amount): PagingRepository<R> {
            return this@mapItemsSuspend.next(amount).mapItemsSuspend(transform)
        }
    }
}
