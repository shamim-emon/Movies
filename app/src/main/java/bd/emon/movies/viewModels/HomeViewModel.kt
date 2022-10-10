package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.movies.base.BaseViewModel
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.entity.Genres
import bd.emon.movies.usecase.GetGenresUseCase
import bd.emon.movies.usecase.PARAM_API_KEY
import bd.emon.movies.usecase.PARAM_LANGUAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getGenresUseCase: GetGenresUseCase) :
    BaseViewModel() {

    var errorState: MutableLiveData<Throwable> = MutableLiveData()
    var genres: MutableLiveData<Genres> = MutableLiveData()

    fun loadGenres(apiKey: String, lang: String) {
        val params = hashMapOf<String, Any>()
        params[PARAM_API_KEY] = apiKey
        params[PARAM_LANGUAGE] = lang

        getGenresUseCase.createObservable(params)
            .subscribe(
                {
                    it.value?.let {
                        genres.postValue(it)
                    } ?: run {
                        errorState.postValue(Throwable(NO_DATA_ERR))
                    }
                },
                {
                    errorState.value = it
                }
            )
    }
}
