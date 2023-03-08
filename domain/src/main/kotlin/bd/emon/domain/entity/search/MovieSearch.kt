package bd.emon.domain.entity.search

data class MovieSearch(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)
