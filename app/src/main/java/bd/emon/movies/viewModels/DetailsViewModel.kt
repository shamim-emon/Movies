package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.entity.details.MovieDetails
import bd.emon.movies.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel() {
    var movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var errorState: MutableLiveData<Throwable> = MutableLiveData()

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
}
