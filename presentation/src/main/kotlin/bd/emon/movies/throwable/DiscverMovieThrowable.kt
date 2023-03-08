package bd.emon.movies.throwable

data class DiscverMovieThrowable(val errorMessage: String, val grp_genre_id: Int) : Throwable()
