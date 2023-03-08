package bd.emon.domain

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.domain.entity.Optional
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
