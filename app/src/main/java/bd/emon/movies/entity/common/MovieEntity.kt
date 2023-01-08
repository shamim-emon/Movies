package bd.emon.movies.entity.common

import bd.emon.movies.common.IMAGE_BASE_URL

data class MovieEntity(
    val id: Int,
    val title: String,
    val poster_path: String,
    var isFav: Boolean = false
) {
    val imageUrl: String
        get() = IMAGE_BASE_URL + poster_path

    val idString: String
        get() = id.toString()
}
