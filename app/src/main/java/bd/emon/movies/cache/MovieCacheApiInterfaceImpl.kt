package bd.emon.movies.cache

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.rxjava3.RxDataStore
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_RELEASE_YEAR
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class MovieCacheApiInterfaceImpl(
    private val store: RxDataStore<Preferences>
) :
    MovieCacheApiInterface {
    override fun saveDiscoverMovieFilters(
        minVoteCount: Int,
        includeAdultContent: Boolean,
        orderBy: String,
        releaseYearStr: String
    ): Single<MutablePreferences> {
        return store.updateDataAsync { pref ->
            val mp: MutablePreferences = pref.toMutablePreferences()
            val PARAM_VOTE_COUNT_GREATER_THAN = intPreferencesKey(PARAM_VOTE_COUNT_GREATER_THAN)
            val PARAM_INCLUDE_ADULT = booleanPreferencesKey(PARAM_INCLUDE_ADULT)
            val PARAM_SORT_BY = stringPreferencesKey(PARAM_SORT_BY)
            val PARAM_RELEASE_YEAR = stringPreferencesKey(PARAM_RELEASE_YEAR)

            mp[PARAM_VOTE_COUNT_GREATER_THAN] = minVoteCount
            mp[PARAM_INCLUDE_ADULT] = includeAdultContent
            mp[PARAM_SORT_BY] = orderBy
            mp[PARAM_RELEASE_YEAR] = releaseYearStr
            Single.just(mp)
        }.map {
            it.toMutablePreferences()
        }
    }

    override fun clearDiscoverMovieFilters(): Single<MutablePreferences> {
        return store.updateDataAsync { pref ->
            val mp: MutablePreferences = pref.toMutablePreferences()
            mp.clear()
            Single.just(mp)
        }.map {
            it.toMutablePreferences()
        }
    }

    override fun getDiscoverMovieFilters(): Flowable<MutablePreferences> {
        return store.data().map {
            it.toMutablePreferences()
        }
    }
}
