package bd.emon.movies.common.dataMapper

import bd.emon.movies.entity.common.MovieEntity
import bd.emon.movies.entity.discover.Result

class DiscoverMovieMapper : Mapper<Result, MovieEntity>() {
    override fun mapFrom(from: Result): MovieEntity {
        return MovieEntity(
            id = from.id,
            title = from.title,
            poster_path = from.poster_path ?: ""
        )
    }

    fun mapFrom(list: MutableList<Result>): MutableList<MovieEntity> {
        val movieList = mutableListOf<MovieEntity>()
        list.forEach {
            movieList.add(mapFrom(it))
        }
        return movieList
    }
}