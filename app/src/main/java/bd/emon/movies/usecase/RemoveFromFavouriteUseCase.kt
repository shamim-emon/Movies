package bd.emon.movies.usecase

import bd.emon.movies.common.PARAM_MOVIE_ENTITY
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.common.MovieEntity
import bd.emon.movies.room.MovieDbRepository
import io.reactivex.rxjava3.core.Observable

class RemoveFromFavouriteUseCase(
    private val movieDbRepository: MovieDbRepository
) : UseCase<Optional<Int>>() {
    lateinit var params: HashMap<String, Any?>
    lateinit var movieEntity: MovieEntity
    fun removeFromFavourite(movieEntity: MovieEntity): Observable<Optional<Int>> {
        this.movieEntity = movieEntity
        params = hashMapOf()
        params[PARAM_MOVIE_ENTITY] = this.movieEntity
        return observable(params)
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<Int>> {
        val movieEntity = withParam?.get(PARAM_MOVIE_ENTITY) as MovieEntity
        var throwable: Throwable
        try {
            return movieDbRepository.removeFromFavourite(movieEntity)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }
}
