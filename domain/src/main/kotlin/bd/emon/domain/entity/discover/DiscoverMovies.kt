package bd.emon.domain.entity.discover

data class DiscoverMovies(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int,
    var grp_genre_id: Int
)
