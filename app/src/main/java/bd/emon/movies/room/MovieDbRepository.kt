package bd.emon.movies.room

import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.common.MovieEntity
import io.reactivex.rxjava3.core.Observable

interface MovieDbRepository {
    fun addToFavourite(movieEntity: MovieEntity): Observable<Optional<Long>>
    fun removeFromFavourite(movieEntity: MovieEntity): Observable<Optional<Int>>
    fun getAllFavourites(): Observable<Optional<List<MovieEntity>>>
    fun getFavouriteMovieById(id: Int): Observable<Optional<MovieEntity>>
}
