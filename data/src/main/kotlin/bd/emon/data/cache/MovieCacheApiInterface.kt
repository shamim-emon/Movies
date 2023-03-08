package bd.emon.data.cache

import androidx.datastore.preferences.core.MutablePreferences
import bd.emon.domain.CURRENT_YEAR
import bd.emon.domain.POPULARITY
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface MovieCacheApiInterface {
    fun saveDiscoverMovieFilters(
        minVoteCount: Int = 0,
        orderBy: String = POPULARITY,
        releaseYearStr: String = CURRENT_YEAR

    ): Single<MutablePreferences>

    fun clearDiscoverMovieFilters(): Single<MutablePreferences>

    fun getDiscoverMovieFilters(): Flowable<MutablePreferences>
}
