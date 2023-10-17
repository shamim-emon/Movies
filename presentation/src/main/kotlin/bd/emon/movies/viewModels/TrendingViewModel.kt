package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.domain.NO_DATA_ERR
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.PARAM_LANGUAGE
import bd.emon.domain.entity.trending.TrendingMovies
import bd.emon.domain.usecase.GetTrendingMoviesUseCase
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.di.qualifier.ApiKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    @ApiKey private val apiKey: String,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : BaseViewModel() {
    var trendingMovies: MutableLiveData<TrendingMovies> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var trendingMoviesErrorState: MutableLiveData<Throwable> = MutableLiveData()
    lateinit var apiParams: HashMap<String, Any?>

    init {
        loadingState.postValue(true)
        initApiParamMap()
    }

    private fun initApiParamMap() {
        apiParams = hashMapOf()
        apiParams[PARAM_API_KEY] = apiKey
    }

    fun loadTrendingMovies(apiKey: String, page: Int) {
        addDisposable(
            getTrendingMoviesUseCase.getTrendingMovies(apiKey, page).subscribe(
                {
                    it.value?.let {
                        trendingMovies.postValue(it)
                    } ?: run {
                        trendingMoviesErrorState.postValue(Throwable(NO_DATA_ERR))
                    }
                    loadingState.postValue(false)
                },
                {
                    trendingMoviesErrorState.postValue(it)
                    loadingState.postValue(false)
                }
            )
        )
    }
}
