package bd.emon.movies.common.paging

interface PagingHelper {
    fun loadNextPage()
    fun hasMoreData(): Boolean
}
