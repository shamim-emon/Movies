package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.entity.search.MovieSearch
import bd.emon.movies.usecase.GetSearchResultUseCase
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase
) : BaseViewModel() {

    var errorState: MutableLiveData<Throwable> = MutableLiveData()
    var movieSearch: MutableLiveData<MovieSearch> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadingState.postValue(true)
    }

    fun searchMovie(
        apiKey: String,
        language: String,
        page: Int,
        includeAdult: Boolean,
        query: String
    ) {
        if (query.length < 3) {
            return
        }
        getSearchResultUseCase.getSearchResult(
            apiKey = apiKey,
            language = language,
            page = page,
            includeAdult = includeAdult,
            query = query
        ).subscribe(
            {
                it.value?.let { result ->
                    movieSearch.postValue(result)
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
    }
}
