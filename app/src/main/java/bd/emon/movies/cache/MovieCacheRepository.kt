package bd.emon.movies.cache

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.movies.common.CURRENT_YEAR
import bd.emon.movies.common.POPULARITY
import bd.emon.movies.entity.Optional
import io.reactivex.rxjava3.core.Observable

interface MovieCacheRepository {
    fun saveDiscoverMovieFilters(
        minVoteCount: Int = 0,
        includeAdultContent: Boolean = false,
        orderBy: String = POPULARITY,
        releaseYearStr: String = CURRENT_YEAR

    ): Observable<Optional<MutablePreferences>>

    fun clearDiscoverFilters(): Observable<Optional<MutablePreferences>>

    fun getDiscoverMovieFilters(): Observable<Optional<MutablePreferences>>
}
