package bd.emon.data.dataMapper

import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.trending.Result

class TrendingMovieMapper : Mapper<Result, MovieEntity>() {
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
