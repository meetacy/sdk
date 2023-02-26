package app.meetacy.types.paging

public data class PagingResponse<T>(
    val nextPagingId: PagingId?,
    val data: T
)
