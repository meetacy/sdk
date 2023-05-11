package app.meetacy.sdk.types.paging

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.amount.amount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.jvm.JvmInline

public inline fun <T> PagingRepository(
    chunkSize: Amount,
    startPagingId: PagingId? = null,
    limit: Amount? = null,
    crossinline provider: suspend (amount: Amount, pagingId: PagingId?) -> PagingResponse<T>
): PagingRepository<T> = PagingRepository(
    provider = object : PagingRepository.Provider<T> {
        override suspend fun get(amount: Amount, pagingId: PagingId?): PagingResponse<T> =
            provider(amount, pagingId)
    },
    chunkSize, startPagingId, limit
)

public class PagingRepository<T>(
    private val provider: Provider<T>,
    private val chunkSize: Amount,
    private val startPagingId: PagingId? = null,
    private val limit: Amount? = null
) {

    public fun <R> map(transform: suspend (T) -> R): PagingRepository<R> {
        return PagingRepository(
            chunkSize = chunkSize,
            startPagingId = startPagingId,
            limit = limit
        ) { amount, pagingId ->
            val response = provider.get(amount, pagingId)
            response.map { data -> transform(data) }
        }
    }

    public fun start(): Started<T> = Started(provider, chunkSize, startPagingId, limit)

    public fun asFlow(): Flow<T> = flow {
        for (data in start()) {
            emit(data)
        }
    }


    public interface Provider<T> {
        public suspend fun get(
            amount: Amount,
            pagingId: PagingId?
        ): PagingResponse<T>
    }

    public class Started<T>(
        private val provider: Provider<T>,
        private val chunkSize: Amount,
        private val startPagingId: PagingId? = null,
        private val limit: Amount? = null
    ) {
        private var loaded: Int = 0
        private var lastResponse: PagingResponse<T>? = null
        private var state = State.NotReady

        public operator fun iterator(): Started<T> = this

        public suspend operator fun hasNext(): Boolean {
            prepareIfNeed()
            return state.isReady
        }

        public suspend operator fun next(): T {
            prepareIfNeed()
            if (state.isDone) throw NoSuchElementException()

            val response = lastResponse ?: error("Stub")

            state = when (response.nextPagingId) {
                null -> State.Done
                else -> State.NotReady
            }

            return response.data
        }

        private suspend fun prepareIfNeed() {
            if (state.isPrepared) return

            val pagingId = lastResponse?.nextPagingId ?: startPagingId
            val most = if (limit != null) (limit.int - loaded) else chunkSize.int
            val amount = chunkSize.int.coerceAtMost(most).amount

            val response = provider.get(amount, pagingId)
            lastResponse = response
            loaded += chunkSize.int
            state = State.Ready
        }
    }

    public class FlatStarted<T>(
        private val base: Started<List<T>>
    ) {
        private val remainingItems: MutableList<T> = mutableListOf()
        private var state = State.NotReady

        public operator fun iterator(): FlatStarted<T> = this

        public suspend fun hasNext(): Boolean {
            prepareIfNeed()
            return remainingItems.isNotEmpty() || base.hasNext()
        }

        public suspend fun next(): T {
            prepareIfNeed()

            if (state.isDone) throw NoSuchElementException()

            if (remainingItems.isEmpty()) {
                state = State.Done
                throw NoSuchElementException()
            }

            val item = remainingItems.removeFirst()

            if (remainingItems.isEmpty()) {
                state = if (base.hasNext()) State.NotReady else State.Done
            }

            return item
        }

        private suspend fun prepareIfNeed() {
            if (state.isPrepared) return
            remainingItems.addAll(base.next())
            state = State.Ready
        }
    }
}

public fun <T> PagingRepository<List<T>>.startFlat(): PagingRepository.FlatStarted<T> =
    PagingRepository.FlatStarted(start())

@JvmInline
private value class State(
    private val int: Int
) {
    init {
        require(int in -1..1)
    }

    val isPrepared: Boolean get() = int != -1
    val isReady: Boolean get() = int == 0
    val isDone: Boolean get() = int == 1

    companion object {
        val NotReady = State(int = -1)
        val Ready = State(int = 0)
        val Done = State(int = 1)
    }
}
