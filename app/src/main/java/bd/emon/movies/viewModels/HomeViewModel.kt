package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.common.MultipleLiveEvent
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_PAGE
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.usecase.GetDiscoverMoviesUseCase
import bd.emon.movies.usecase.GetGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase
) :
    BaseViewModel() {

    var genres: MutableLiveData<Genres> = MutableLiveData()
    var discoverMovies: MultipleLiveEvent<DiscoverMovie> = MultipleLiveEvent()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var errorState: MutableLiveData<Throwable> = MutableLiveData()

    init {
        loadingState.postValue(true)
    }

    fun loadGenres(apiKey: String, lang: String) {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = apiKey
        params[PARAM_LANGUAGE] = lang
        addDisposable(
            getGenresUseCase.getGenres(params)
                .subscribe(
                    {
                        it.value?.let {
                            genres.postValue(it)
                        } ?: run {
                            errorState.postValue(Throwable(NO_DATA_ERR))
                        }
                        loadingState.postValue(false)
                    },
                    {
                        errorState.postValue(it)
                        loadingState.postValue(false)
                    }
                )
        )
    }

    fun loadDiscoverMovies(
        apiKey: String,
        lang: String,
        genres: Int,
        sortBy: String? = null,
        includeAdult: Boolean? = null,
        page: Int? = null,
        voteCountGreaterThan: Int? = null,
    ) {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = apiKey
        params[PARAM_LANGUAGE] = lang
        params[PARAM_SORT_BY] = sortBy
        params[PARAM_INCLUDE_ADULT] = includeAdult
        params[PARAM_PAGE] = page
        params[PARAM_VOTE_COUNT_GREATER_THAN] = voteCountGreaterThan
        params[PARAM_GENRES] = genres
        addDisposable(
            getDiscoverMoviesUseCase.getDiscoverMovies(params)
                .subscribe(
                    {
                        it.value?.let { data ->
                            discoverMovies.postValue(data)
                        } ?: run {
                            errorState.postValue(Throwable(NO_DATA_ERR))
                        }
                        loadingState.postValue(false)
                    },
                    {
                        errorState.postValue(it)
                        loadingState.postValue(false)
                    }
                )
        )
    }
}
