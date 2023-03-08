package bd.emon.domain.usecase

import bd.emon.domain.MovieDbRepository
import bd.emon.domain.PARAM_MOVIE_ENTITY
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.common.MovieEntity
import io.reactivex.rxjava3.core.Observable

class AddToFavouriteUseCase(
    private val movieDbRepository: MovieDbRepository
) : UseCase<Optional<Long>>() {
    lateinit var params: HashMap<String, Any?>
    lateinit var movieEntity: MovieEntity
    fun addToFavourite(movieEntity: MovieEntity): Observable<Optional<Long>> {
        this.movieEntity = movieEntity
        params = hashMapOf()
        params[PARAM_MOVIE_ENTITY] = this.movieEntity
        return observable(params)
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<Long>> {
        val movieEntity = withParam?.get(PARAM_MOVIE_ENTITY) as MovieEntity
        var throwable: Throwable
        try {
            return movieDbRepository.addToFavourite(movieEntity)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }
}
