package bd.emon.data.cache

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.domain.MovieCacheRepository
import bd.emon.domain.SchedulerProvider
import bd.emon.domain.entity.Optional
import io.reactivex.rxjava3.core.Observable

class MovieCacheRepositoryImpl(
    private val schedulerProvider: SchedulerProvider,
    private val movieCacheApiInterface: MovieCacheApiInterface
) :
    MovieCacheRepository {

    override fun saveDiscoverMovieFilters(
        minVoteCount: Int,
        includeAdultContent: Boolean,
        orderBy: String,
        releaseYearStr: String
    ): Observable<Optional<MutablePreferences>> {
        return movieCacheApiInterface.saveDiscoverMovieFilters(
            minVoteCount,
            orderBy,
            releaseYearStr
        )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map {
                Optional.of(it)
            }.toObservable()
    }

    override fun clearDiscoverFilters(): Observable<Optional<MutablePreferences>> {
        return movieCacheApiInterface.clearDiscoverMovieFilters().map {
            Optional.of(it)
        }.toObservable()
    }

    override fun getDiscoverMovieFilters(): Observable<Optional<MutablePreferences>> {
        return movieCacheApiInterface.getDiscoverMovieFilters().map {
            Optional.of(it)
        }.toObservable()
    }
}
