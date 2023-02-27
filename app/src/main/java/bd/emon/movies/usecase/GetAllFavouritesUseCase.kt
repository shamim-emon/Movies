package bd.emon.movies.usecase

import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.common.MovieEntity
import bd.emon.movies.room.MovieDbRepository
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
