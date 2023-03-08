package bd.emon.movies.viewModels

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import bd.emon.domain.CURRENT_YEAR
import bd.emon.domain.DEFAULT_ORDER_BY
import bd.emon.domain.DESC
import bd.emon.domain.NO_DATA_ERR
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.PARAM_GENRES
import bd.emon.domain.PARAM_INCLUDE_ADULT
import bd.emon.domain.PARAM_LANGUAGE
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.domain.usecase.ClearCacheDiscoverMoviesFiltersUseCase
import bd.emon.domain.usecase.GetCacheDiscoverMovieFilterUseCase
import bd.emon.domain.usecase.GetDiscoverMoviesUseCase
import bd.emon.domain.usecase.GetGenresUseCase
import bd.emon.domain.usecase.SaveCacheDiscoverMoviesFiltersUseCase
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.common.MultipleLiveEvent
import bd.emon.movies.di.qualifier.ApiKey
import bd.emon.movies.di.qualifier.AppLanguage
import bd.emon.movies.throwable.DiscverMovieThrowable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase,
    private val saveCacheDiscoverMoviesFiltersUseCase: SaveCacheDiscoverMoviesFiltersUseCase,
    private val getCacheDiscoverMovieFilterUseCase: GetCacheDiscoverMovieFilterUseCase,
    private val clearCacheDiscoverMovieFilterUseCase: ClearCacheDiscoverMoviesFiltersUseCase,
    @ApiKey private val apiKey: String,
    @AppLanguage private val language: String
) :
    BaseViewModel() {

    lateinit var apiParams: HashMap<String, Any?>
    var genres: MutableLiveData<Genres> = MutableLiveData()
    var discoverMovies: MultipleLiveEvent<DiscoverMovies> =
        MultipleLiveEvent()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var genreErrorState: MutableLiveData<Throwable> = MutableLiveData()
    var discoverMoviesErrorState: MutableLiveData<DiscverMovieThrowable> = MutableLiveData()
    var loadDiscoverFilters: MutableLiveData<MutablePreferences> = MutableLiveData()
    var saveDiscoverFilters: MutableLiveData<MutablePreferences> = MutableLiveData()
    var clearDiscoverFilters: MutableLiveData<MutablePreferences> = MutableLiveData()
    var discoverFiltersErrorState: MutableLiveData<Throwable> = MutableLiveData()

    init {
        loadingState.postValue(true)
        initApiParamMap()
    }

    private fun initApiParamMap() {
        apiParams = hashMapOf()
        apiParams[PARAM_API_KEY] = apiKey
        apiParams[PARAM_LANGUAGE] = language
    }

    fun loadGenres(params: HashMap<String, Any?>) {
        addDisposable(
            getGenresUseCase.getGenres(params)
                .subscribe(
                    {
                        it.value?.let {
                            genres.postValue(it)
                        } ?: run {
                            genreErrorState.postValue(Throwable(NO_DATA_ERR))
                        }
                        loadingState.postValue(false)
                    },
                    {
                        genreErrorState.postValue(it)
                        loadingState.postValue(false)
                    }
                )
        )
    }

    fun loadDiscoverMovies(
        params: HashMap<String, Any?>,
        page: Int
    ) {
        addDisposable(
            getDiscoverMoviesUseCase.getDiscoverMovies(params, page)
                .subscribe(
                    {
                        it.value?.let { data ->
                            discoverMovies.postValue(data)
                        } ?: run {
                            discoverMoviesErrorState.postValue(
                                DiscverMovieThrowable(
                                    NO_DATA_ERR,
                                    params[PARAM_GENRES] as Int
                                )
                            )
                        }
                        loadingState.postValue(false)
                    },
                    {
                        discoverMoviesErrorState.postValue(
                            DiscverMovieThrowable(it.localizedMessage, params[PARAM_GENRES] as Int)
                        )
                        loadingState.postValue(false)
                    }
                )
        )
    }

    fun saveDiscoverMoviesFilters(
        minVoteCount: Int = 0,
        includeAdultContent: Boolean = false,
        orderBy: String = DEFAULT_ORDER_BY,
        releaseYearStr: String = CURRENT_YEAR
    ) {
        saveCacheDiscoverMoviesFiltersUseCase.cacheFilterParams(
            minVoteCount = minVoteCount,
            includeAdultContent = includeAdultContent,
            orderBy = orderBy,
            releaseYearStr = releaseYearStr
        ).subscribe(
            {
                it.value?.let { data ->
                    saveDiscoverFilters.postValue(data)
                }
            },
            {
                discoverFiltersErrorState.postValue(it)
            }
        )
    }

    fun loadDiscoverMovieFiltersAndHoldInApiParamMap() {
        getCacheDiscoverMovieFilterUseCase.getDiscoverMovieFilters()
            .subscribe {
                var mp: MutablePreferences = mutablePreferencesOf()
                it.value?.let { data ->
                    mp = data
                }

                val VOTE_COUNT_GREATER_THAN = intPreferencesKey(PARAM_VOTE_COUNT_GREATER_THAN)
                val INCLUDE_ADULT = booleanPreferencesKey(PARAM_INCLUDE_ADULT)
                val SORT_BY = stringPreferencesKey(PARAM_SORT_BY)
                val RELEASE_YEAR = stringPreferencesKey(PARAM_RELEASE_YEAR)

                mp[SORT_BY]?.let {
                    apiParams[PARAM_SORT_BY] = "$it.$DESC"
                } ?: run {
                    apiParams[PARAM_SORT_BY] = DEFAULT_ORDER_BY
                }

                mp[VOTE_COUNT_GREATER_THAN]?.let {
                    apiParams[PARAM_VOTE_COUNT_GREATER_THAN] = it
                } ?: run {
                    apiParams[PARAM_VOTE_COUNT_GREATER_THAN] = 0
                }

                mp[INCLUDE_ADULT]?.let {
                    apiParams[PARAM_INCLUDE_ADULT] = it
                } ?: run {
                    apiParams[PARAM_INCLUDE_ADULT] = false
                }

                mp[RELEASE_YEAR]?.let {
                    when (it) {
                        CURRENT_YEAR -> apiParams[PARAM_RELEASE_YEAR] = null
                        else -> apiParams[PARAM_RELEASE_YEAR] = it
                    }
                }

                loadDiscoverFilters.postValue(it.value ?: mutablePreferencesOf())
            }
    }

    fun clearFilterParams() {
        clearCacheDiscoverMovieFilterUseCase.clearFilterParams()
            .subscribe {
                it.value?.let { data ->
                    initApiParamMap()
                    clearDiscoverFilters.postValue(data)
                }
            }
    }
}
