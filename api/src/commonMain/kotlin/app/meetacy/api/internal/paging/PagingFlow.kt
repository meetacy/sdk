package app.meetacy.api.internal.paging

import app.meetacy.types.amount.Amount
import app.meetacy.types.amount.amount
import app.meetacy.types.paging.PagingId
import app.meetacy.types.paging.PagingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal inline fun <T> pagingFlow(
    chunkSize: Amount,
    startPagingId: PagingId? = null,
    limit: Amount? = null,
    crossinline block: suspend (pagingId: PagingId?, chunkSize: Amount) -> PagingResponse<T>
): Flow<PagingResponse<T>> = flow {
    var currentPageId = startPagingId
    var currentLimit = limit

    while (true) {
        val currentChunkSize = currentLimit?.int?.coerceAtMost(chunkSize.int)?.amount ?: chunkSize

        val response = block(currentPageId, currentChunkSize)
        currentPageId = response.nextPagingId ?: break

        if (currentLimit == null) continue
        if (currentLimit.int - chunkSize.int <= 0) break
        currentLimit = (currentLimit.int - chunkSize.int).amount
    }
}
