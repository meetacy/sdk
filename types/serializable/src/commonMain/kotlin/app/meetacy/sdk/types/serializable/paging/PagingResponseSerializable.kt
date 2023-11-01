package app.meetacy.sdk.types.serializable.paging

import app.meetacy.sdk.types.paging.PagingResponse
import kotlinx.serialization.Serializable

@Serializable
public data class PagingResponseSerializable<out T>(
    val data: List<T>,
    val nextPagingId: PagingIdSerializable?
)

public fun <T> PagingResponseSerializable<T>.type(): PagingResponse<T> =
    PagingResponse(data, nextPagingId?.type())
public fun <T> PagingResponse<T>.serializable(): PagingResponseSerializable<T> =
    PagingResponseSerializable(data, nextPagingId?.serializable())
