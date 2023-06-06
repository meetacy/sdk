package app.meetacy.sdk.types.paging

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.amount.amount

public interface PagingIterator<T> {
    public suspend operator fun hasNext(): Boolean
    public suspend operator fun next(): List<T>

    public operator fun iterator(): PagingIterator<T> = this
}

@Suppress("ClassName")
internal class _PagingIterator<T>(
    private val chunkSize: Amount,
    private val startPagingId: PagingId? = null,
    private val limit: Amount? = null,
    private val provider: suspend (amount: Amount, pagingId: PagingId?) -> PagingResponse<T>
) : PagingIterator<T> {

    // State:
    // -1 – unknown
    // 0 – ready
    // 1 – done
    private var state = -1
    private var response: PagingResponse<T>? = null
    private var loaded = 0

    override suspend fun hasNext(): Boolean {
        prepareIfNeed()
        return state == 0
    }

    override suspend fun next(): List<T> {
        prepareIfNeed()
        if (state == 1) throw NoSuchElementException()
        state = -1
        return response?.data ?: throw NoSuchElementException()
    }

    private suspend fun prepareIfNeed() {
        if (state != -1) return

        val response = response

        if (response != null && response.nextPagingId == null) {
            state = 1
            return
        }

        val amount = when (limit) {
            null -> chunkSize.int
            else -> chunkSize.int.coerceAtMost(maximumValue = limit.int - loaded)
        }

        this.response = when (response) {
            null -> provider(amount.amount, startPagingId)
            else -> provider(amount.amount, response.nextPagingId)
        }

        state = 0
    }

}
