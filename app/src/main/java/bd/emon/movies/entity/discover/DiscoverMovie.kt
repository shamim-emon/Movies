package bd.emon.movies.entity.discover

data class DiscoverMovie(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int,
    var grp_genre_id: Int
)
