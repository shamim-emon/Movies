package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.common.MultipleLiveEvent
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.entity.discover.DiscoverMovie
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.throwable.DiscverMovieThrowable
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
    var genreErrorState: MutableLiveData<Throwable> = MutableLiveData()
    var discoverMoviesErrorState: MutableLiveData<DiscverMovieThrowable> = MutableLiveData()

    init {
        loadingState.postValue(true)
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
        params: HashMap<String, Any?>
    ) {
        addDisposable(
            getDiscoverMoviesUseCase.getDiscoverMovies(params)
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
}
