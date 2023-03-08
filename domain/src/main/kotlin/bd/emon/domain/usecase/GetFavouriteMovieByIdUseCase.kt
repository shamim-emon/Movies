package bd.emon.domain.usecase

import bd.emon.domain.MovieDbRepository
import bd.emon.domain.PARAM_MOVIE_ID
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.common.MovieEntity
import io.reactivex.rxjava3.core.Observable

class GetFavouriteMovieByIdUseCase(
    private val movieDbRepository: MovieDbRepository
) : UseCase<Optional<MovieEntity>>() {
    var id: Int = -1
    private val param: HashMap<String, Any?> = hashMapOf()

    fun getFavouriteMovieById(id: Int): Observable<Optional<MovieEntity>> {
        this.id = id
        param[PARAM_MOVIE_ID] = this.id
        return observable(param)
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<MovieEntity>> {
        var throwable: Throwable
        try {
            val movieId = withParam?.get(PARAM_MOVIE_ID) as Int
            return movieDbRepository.getFavouriteMovieById(movieId)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }
}
