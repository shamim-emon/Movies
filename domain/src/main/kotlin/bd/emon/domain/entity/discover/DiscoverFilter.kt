package bd.emon.domain.entity.discover

data class DiscoverFilter(
    val minVote: Int,
    val includeAdult: Boolean,
    val sortType: String,
    val sortBy: String,
    val releaseYear: String
)
