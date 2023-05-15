package app.meetacy.sdk.types.paging

/**
 * Note: You can conveniently use result with component1 operator
 *
 * val (users) = api.users.list(...)
 */
public data class PagingResponse<out T>(
    val data: List<T>,
    val nextPagingId: PagingId?
) {
    public inline fun <R> map(transform: (List<T>) -> List<R>): PagingResponse<R> =
        PagingResponse(
            data = transform(data),
            nextPagingId = nextPagingId
        )

    public inline fun <R> mapItems(transform: (T) -> R): PagingResponse<R> =
        map { list -> list.map(transform) }
}
