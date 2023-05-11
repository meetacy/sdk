package app.meetacy.sdk.internal.paging

import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.amount.amount
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal inline fun <T> pagingFlow(
    chunkSize: Amount,
    startPagingId: PagingId? = null,
    limit: Amount? = null,
    crossinline block: suspend (pagingId: PagingId?, chunkSize: Amount) -> PagingResponse<T>
): Flow<T> = flow {
    var currentPageId = startPagingId
    var currentLimit = limit

    while (true) {
        val currentChunkSize = currentLimit?.int?.coerceAtMost(chunkSize.int)?.amount ?: chunkSize

        val response = block(currentPageId, currentChunkSize)
        emit(response.data)
        currentPageId = response.nextPagingId ?: break

        if (currentLimit == null) continue
        if (currentLimit.int - chunkSize.int <= 0) break
        currentLimit = (currentLimit.int - chunkSize.int).amount
    }
}
