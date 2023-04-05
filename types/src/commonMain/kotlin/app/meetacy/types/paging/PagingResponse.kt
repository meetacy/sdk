package app.meetacy.types.paging

/**
 * Note: You can conveniently use result with component1 operator
 *
 * val (users) = api.users.list(...)
 */
public data class PagingResponse<T>(
    val data: T,
    val nextPagingId: PagingId?
) {
    public inline fun <R> map(transform: (T) -> R): PagingResponse<R> =
        PagingResponse(
            data = transform(data),
            nextPagingId = nextPagingId
        )
}


public inline fun <T, R> PagingResponse<List<T>>.mapItems(transform: (T) -> R): PagingResponse<List<R>> =
    map { list ->
        list.map(transform)
    }
