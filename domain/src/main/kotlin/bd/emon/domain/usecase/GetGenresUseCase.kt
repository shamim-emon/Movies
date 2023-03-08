package bd.emon.domain.usecase

import bd.emon.domain.MovieRestRepository
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.genre.Genres
import io.reactivex.rxjava3.core.Observable

open class GetGenresUseCase(
    private val movieRestRepository: MovieRestRepository
) : UseCase<Optional<Genres>>() {
    var params: HashMap<String, Any?>? = null
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<Genres>> {
        params = withParam
        var throwable: Throwable
        try {
            return movieRestRepository.getGenres(params as Map<String, Any?>)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }

    fun getGenres(withParam: HashMap<String, Any?>?): Observable<Optional<Genres>> {
        return observable(withParam)
    }
}
