package bd.emon.domain.paging

interface PagingHelper {
    fun loadNextPage()
    fun hasMoreData(): Boolean
}
