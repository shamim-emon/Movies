package bd.emon.domain.usecase

import bd.emon.domain.MovieDbRepository
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.common.MovieEntity
import io.reactivex.rxjava3.core.Observable

class GetAllFavouritesUseCase(
    private val movieDbRepository: MovieDbRepository
) : UseCase<Optional<List<MovieEntity>>>() {

    fun getAllFavourites(): Observable<Optional<List<MovieEntity>>> {
        return observable()
    }

    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<List<MovieEntity>>> {
        var throwable: Throwable
        try {
            return movieDbRepository.getAllFavourites()
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }
}
