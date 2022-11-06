package bd.emon.movies.usecase

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.movies.cache.MovieCacheRepository
import bd.emon.movies.common.Transformer
import bd.emon.movies.entity.Optional
import io.reactivex.rxjava3.core.Observable

class GetCacheDiscoverMovieFilterUseCase(
    transformer: Transformer<Optional<MutablePreferences>>,
    private val movieCacheRepository: MovieCacheRepository
) : UseCase<Optional<MutablePreferences>>(transformer) {
    override fun createObservable(withParam: HashMap<String, Any?>?): Observable<Optional<MutablePreferences>> {
        var throwable: Throwable
        try {
            return movieCacheRepository.getDiscoverMovieFilters()
        } catch (t: Throwable) {
            throwable = t
        }

        return Observable.error(throwable)
    }

    fun getDiscoverMovieFilters(): Observable<Optional<MutablePreferences>> {
        return observable()
    }
}
