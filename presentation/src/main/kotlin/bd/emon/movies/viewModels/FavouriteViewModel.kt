package bd.emon.movies.viewModels

import androidx.lifecycle.MutableLiveData
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.usecase.AddToFavouriteUseCase
import bd.emon.domain.usecase.GetAllFavouritesUseCase
import bd.emon.domain.usecase.GetFavouriteMovieByIdUseCase
import bd.emon.domain.usecase.RemoveFromFavouriteUseCase
import bd.emon.movies.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val removeFromFavouriteUseCase: RemoveFromFavouriteUseCase,
    private val getAllFavouritesUseCase: GetAllFavouritesUseCase,
    private val getFavouriteMovieByIdUseCase: GetFavouriteMovieByIdUseCase
) : BaseViewModel() {
    var addToFavouriteState: MutableLiveData<Long> = MutableLiveData()
    var removeFromFavouriteState: MutableLiveData<Int> = MutableLiveData()
    var getAllFavouriteState: MutableLiveData<List<MovieEntity>> =
        MutableLiveData()
    var getFavouriteMovieByIdState: MutableLiveData<MovieEntity?> =
        MutableLiveData()
    val errorState: MutableLiveData<Throwable> = MutableLiveData()
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadingState = MutableLiveData(true)
    }

    fun addToFavourite(movieEntity: MovieEntity) {
        addDisposable(
            addToFavouriteUseCase.addToFavourite(movieEntity)
                .subscribe(
                    {
                        addToFavouriteState.postValue(it.value!!)
                        loadingState.postValue(false)
                    },
                    {
                        errorState.postValue(it)
                        loadingState.postValue(false)
                    }
                )
        )
    }

    fun removeFromFavourite(movieEntity: MovieEntity) {
        addDisposable(
            removeFromFavouriteUseCase.removeFromFavourite(movieEntity)
                .subscribe(
                    {
                        removeFromFavouriteState.postValue(it.value!!)
                        loadingState.postValue(false)
                    },
                    {
                        errorState.postValue(it)
                        loadingState.postValue(false)
                    }
                )
        )
    }

    fun getAllFavs() {
        addDisposable(
            getAllFavouritesUseCase.getAllFavourites()
                .subscribe(
                    {
                        it.value?.let { data ->
                            getAllFavouriteState.postValue(data)
                        } ?: run {
                            getAllFavouriteState.postValue(mutableListOf())
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

    fun getFavMovieById(id: Int) {
        addDisposable(
            getFavouriteMovieByIdUseCase.getFavouriteMovieById(id)
                .subscribe(
                    {
                        it.value?.let { data ->
                            getFavouriteMovieByIdState.postValue(data)
                        } ?: run {
                            getFavouriteMovieByIdState.postValue(null)
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
