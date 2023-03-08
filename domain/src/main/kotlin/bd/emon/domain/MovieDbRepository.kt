package bd.emon.domain

import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.common.MovieEntity
import io.reactivex.rxjava3.core.Observable

interface MovieDbRepository {
    fun addToFavourite(movieEntity: MovieEntity): Observable<Optional<Long>>
    fun removeFromFavourite(movieEntity: MovieEntity): Observable<Optional<Int>>
    fun getAllFavourites(): Observable<Optional<List<MovieEntity>>>
    fun getFavouriteMovieById(id: Int): Observable<Optional<MovieEntity>>
}
