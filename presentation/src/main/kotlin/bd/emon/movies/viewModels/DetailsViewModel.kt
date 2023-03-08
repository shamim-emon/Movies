package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.domain.entity.details.MovieDetails
import bd.emon.domain.entity.details.MovieVideos
import bd.emon.domain.usecase.GetMovieDetailsUseCase
import bd.emon.domain.usecase.GetMovieVideosUseCase
import bd.emon.movies.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase
) : BaseViewModel() {
    var movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var errorState: MutableLiveData<Throwable> = MutableLiveData()
    var movieVideos: MutableLiveData<MovieVideos> = MutableLiveData()

    init {
        loadingState = MutableLiveData(true)
    }

    fun getMovieDetails(apiKey: String, language: String, movieId: String) {
        addDisposable(
            getMovieDetailsUseCase
                .getMovieDetails(apiKey, language, movieId)
                .subscribe(
                    {
                        movieDetails.postValue(it.value!!)
                        loadingState.postValue(false)
                    },
                    {
                        errorState.postValue(it)
                        loadingState.postValue(false)
                    }
                )
        )
    }

    fun getMovieVideos(apiKey: String, movieId: String) {
        addDisposable(
            getMovieVideosUseCase.getMovieVideos(apiKey, movieId)
                .subscribe(
                    {
                        movieVideos.postValue(it.value!!)
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
