package bd.emon.movies.entity.trending

data class TrendingMovies(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)
