package bd.emon.movies.usecase

import bd.emon.movies.common.Transformer
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.rest.MovieRepository
import io.reactivex.rxjava3.core.Observable

open class GetGenresUseCase(
    transformer: Transformer<Optional<Genres>>,
    private val movieRepository: MovieRepository
) : UseCase<Optional<Genres>>(transformer) {
    var params: HashMap<String, Any?>? = null
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<Genres>> {
        params = withParam
        var throwable: Throwable
        try {
            return movieRepository.getGenres(params as Map<String, Any?>)
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }

    fun getGenres(withParam: HashMap<String, Any?>?): Observable<Optional<Genres>>{
        return observable(withParam)
    }
}
