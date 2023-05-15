package app.meetacy.sdk.types.paging

public interface FlatPagingIterator<T> {
    public suspend operator fun hasNext(): Boolean
    public suspend operator fun next(): T

    public operator fun iterator(): FlatPagingIterator<T> = this
}

@Suppress("ClassName")
internal class _FlatPagingIterator<T>(private val base: PagingIterator<T>) : FlatPagingIterator<T> {
    private val items: MutableList<T> = mutableListOf()
    // State
    // -1 – unknown
    // 0 - ready
    // 1 - done
    private var state: Int = -1

    override suspend fun hasNext(): Boolean {
        prepareIfNeed()
        return state == 0
    }

    override suspend fun next(): T {
        prepareIfNeed()
        if (state == 1) throw NoSuchElementException()
        val element = items.removeFirst()
        if (items.isEmpty()) {
            state = -1
        }
        return element
    }

    private suspend fun prepareIfNeed() {
        if (state != -1) return

        if (!base.hasNext()) {
            state = 1
            return
        }

        val next = base.next()

        if (next.isEmpty()) {
            state = -1
            return prepareIfNeed()
        }

        items.addAll(next)
        state = 0
    }
}
