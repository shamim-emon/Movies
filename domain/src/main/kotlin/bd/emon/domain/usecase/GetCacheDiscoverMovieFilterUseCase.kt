package bd.emon.domain.usecase

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.domain.MovieCacheRepository
import bd.emon.domain.entity.Optional
import io.reactivex.rxjava3.core.Observable

class GetCacheDiscoverMovieFilterUseCase(
    private val movieCacheRepository: MovieCacheRepository
) : UseCase<Optional<MutablePreferences>>() {
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
