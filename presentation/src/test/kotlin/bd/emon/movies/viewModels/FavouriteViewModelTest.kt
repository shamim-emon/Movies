package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.domain.DB_TRANSACTION_ERROR
import bd.emon.domain.entity.Optional
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.usecase.AddToFavouriteUseCase
import bd.emon.domain.usecase.GetAllFavouritesUseCase
import bd.emon.domain.usecase.GetFavouriteMovieByIdUseCase
import bd.emon.domain.usecase.RemoveFromFavouriteUseCase
import bd.emon.movies.any
import bd.emon.movies.capture
import io.reactivex.rxjava3.core.Observable
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavouriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    lateinit var addToFavouriteUseCase: AddToFavouriteUseCase
    lateinit var removeFromFavouriteUseCase: RemoveFromFavouriteUseCase
    lateinit var getAllFavouritesUseCase: GetAllFavouritesUseCase
    lateinit var getFavouriteMovieByIdUseCase: GetFavouriteMovieByIdUseCase
    lateinit var viewModel: FavouriteViewModel
    val ADD_TO_FAV_SUCCESS = 1L
    val REMOVE_FROM_FAV_SUCCESS = 1
    val MOVIE_ID = 960704
    val MOVIE_ENTITY = MovieEntity(
        id = 960704,
        poster_path = "/AeyiuQUUs78bPkz18FY3AzNFF8b.jpg",
        title = "Fullmetal Alchemist: The Final Alchemy"
    )

    val movieEntityList = listOf(
        MovieEntity(
            id = 960704,
            poster_path = "/AeyiuQUUs78bPkz18FY3AzNFF8b.jpg",
            title = "Fullmetal Alchemist: The Final Alchemy"
        ),
        MovieEntity(
            id = 960705,
            poster_path = "/AeyiuQUUs78bPkz18FY3AzNFF8b.jpg",
            title = "Fullmetal Alchemist2: The Final Alchemy"
        )
    )

    @Mock
    lateinit var movieDbRepository: bd.emon.domain.MovieDbRepository

    @Captor
    lateinit var movieEntityCaptor: ArgumentCaptor<MovieEntity>

    @Captor
    lateinit var intCaptor: ArgumentCaptor<Int>

    @Before
    fun setUp() {
        addToFavouriteUseCase = AddToFavouriteUseCase(movieDbRepository)
        removeFromFavouriteUseCase = RemoveFromFavouriteUseCase(movieDbRepository)
        getAllFavouritesUseCase = GetAllFavouritesUseCase(movieDbRepository)
        getFavouriteMovieByIdUseCase = GetFavouriteMovieByIdUseCase(movieDbRepository)
        viewModel = FavouriteViewModel(
            addToFavouriteUseCase,
            removeFromFavouriteUseCase,
            getAllFavouritesUseCase,
            getFavouriteMovieByIdUseCase
        )
        addToFavourite_success()
        removeFromFavourite_success()
        getAllFavourite_success_withData()
        getFavMovieById_success()
    }

    @Test
    fun addToFavourite_correctParamsPassedToUseCase() {
        viewModel.addToFavourite(MOVIE_ENTITY)
        assertThat(addToFavouriteUseCase.movieEntity, `is`(MOVIE_ENTITY))
    }

    @Test
    fun addToFavourite_correctParamsPassedToRepository() {
        viewModel.addToFavourite(MOVIE_ENTITY)
        assertThat(addToFavouriteUseCase.movieEntity, CoreMatchers.`is`(MOVIE_ENTITY))
        verify(movieDbRepository, times(1))
            .addToFavourite(capture(movieEntityCaptor))
        assertThat(movieEntityCaptor.value, `is`(MOVIE_ENTITY))
    }

    @Test
    fun addToFavourite_success_successEmitted() {
        viewModel.addToFavourite(MOVIE_ENTITY)
        assertThat(viewModel.addToFavouriteState.value, `is`(ADD_TO_FAV_SUCCESS))
    }

    @Test
    fun addToFavourite_success_loadStateFalseEmitted() {
        viewModel.addToFavourite(MOVIE_ENTITY)
        assertThat(viewModel.loadingState.value, `is`(false))
    }

    @Test
    fun addToFavourite_error_errorMessageEmitted() {
        addToFavourite_error()
        viewModel.addToFavourite(MOVIE_ENTITY)
        assertThat(viewModel.errorState.value!!.message, `is`(DB_TRANSACTION_ERROR))
    }

    @Test
    fun addToFavourite_error_loadStateFalseEmitted() {
        addToFavourite_error()
        viewModel.addToFavourite(MOVIE_ENTITY)
        assertThat(viewModel.loadingState.value!!, `is`(false))
    }

    @Test
    fun removeFromFavourite_correctParamsPassedToUseCase() {
        viewModel.removeFromFavourite(MOVIE_ENTITY)
        assertThat(removeFromFavouriteUseCase.movieEntity, `is`(MOVIE_ENTITY))
    }

    @Test
    fun removeFromFavourite_correctParamsPassedToRepository() {
        viewModel.removeFromFavourite(MOVIE_ENTITY)
        assertThat(removeFromFavouriteUseCase.movieEntity, CoreMatchers.`is`(MOVIE_ENTITY))
        verify(movieDbRepository, times(1))
            .removeFromFavourite(capture(movieEntityCaptor))
        assertThat(movieEntityCaptor.value, `is`(MOVIE_ENTITY))
    }

    @Test
    fun removeFromFavourite_success_successEmitted() {
        viewModel.removeFromFavourite(MOVIE_ENTITY)
        assertThat(viewModel.removeFromFavouriteState.value, `is`(REMOVE_FROM_FAV_SUCCESS))
    }

    @Test
    fun removeFromFavourite_success_loadStateFalseEmitted() {
        viewModel.removeFromFavourite(MOVIE_ENTITY)
        assertThat(viewModel.loadingState.value, `is`(false))
    }

    @Test
    fun removeFromFavourite_error_errorMessageEmitted() {
        removeFromFavourite_error()
        viewModel.removeFromFavourite(MOVIE_ENTITY)
        assertThat(viewModel.errorState.value!!.message, `is`(DB_TRANSACTION_ERROR))
    }

    @Test
    fun removeFromFavourite_error_loadStateFalseEmitted() {
        removeFromFavourite_error()
        viewModel.removeFromFavourite(MOVIE_ENTITY)
        assertThat(viewModel.loadingState.value!!, `is`(false))
    }

    @Test
    fun getAllFavs_success_FavListEmitted() {
        viewModel.getAllFavs()
        assertThat(viewModel.getAllFavouriteState.value, `is`(movieEntityList))
    }

    @Test
    fun getAllFavs_success_emptyListEmitted() {
        getAllFavourite_success_withNoData()
        viewModel.getAllFavs()
        assertThat(viewModel.getAllFavouriteState.value!!.size, `is`(0))
    }

    @Test
    fun getAllFavs_success_loadStateFalseEmitted() {
        viewModel.getAllFavs()
        assertThat(viewModel.loadingState.value, `is`(false))
    }

    @Test
    fun getAllFavs_error_nullEmitted() {
        getAllFavourite_error()
        viewModel.getAllFavs()
        assertThat(viewModel.getAllFavouriteState.value, `is`(nullValue()))
    }

    @Test
    fun getAllFavs_error_ErrorMessageEmitted() {
        getAllFavourite_error()
        viewModel.getAllFavs()
        assertThat(viewModel.errorState.value!!.message, `is`(DB_TRANSACTION_ERROR))
    }

    @Test
    fun getAllFavs_error_loadStateFalseEmitted() {
        getAllFavourite_error()
        viewModel.getAllFavs()
        assertThat(viewModel.loadingState.value, `is`(false))
    }

    @Test
    fun getFavouriteMovieById_correctParamPassedToUseCase() {
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(getFavouriteMovieByIdUseCase.id, `is`(MOVIE_ID))
    }

    @Test
    fun getFavouriteMovieById_correctParamPassedToRepository() {
        viewModel.getFavMovieById(MOVIE_ID)
        verify(movieDbRepository, times(1))
            .getFavouriteMovieById(capture(intCaptor))
        assertThat(intCaptor.value, `is`(MOVIE_ID))
    }

    @Test
    fun getFavouriteMovieById_success_movieEntiryEmitted() {
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(viewModel.getFavouriteMovieByIdState.value, `is`(MOVIE_ENTITY))
    }

    @Test
    fun getFavouriteMovieById_success_nullEmitted() {
        getFavMovieById_success_noData()
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(viewModel.getFavouriteMovieByIdState.value, `is`(nullValue()))
    }

    @Test
    fun getFavouriteMovieById_success_loadStateFalseEmitted() {
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(viewModel.loadingState.value, `is`(false))
    }

    @Test
    fun getFavouriteMovieById_erorr_nullEmitted() {
        getFavMovieById_error()
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(viewModel.getFavouriteMovieByIdState.value, `is`(nullValue()))
    }

    @Test
    fun getFavouriteMovieById_erorr_ErrorMessageEmitted() {
        getFavMovieById_error()
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(viewModel.errorState.value!!.message, `is`(DB_TRANSACTION_ERROR))
    }

    @Test
    fun getFavouriteMovieById_erorr_loadStateEmitted() {
        getFavMovieById_error()
        viewModel.getFavMovieById(MOVIE_ID)
        assertThat(viewModel.loadingState.value!!, `is`(false))
    }

    //region helper methods
    fun addToFavourite_success() {
        `when`(
            movieDbRepository.addToFavourite(
                any(MovieEntity::class.java)
            )
        ).thenReturn(
            Observable.just(Optional.of(ADD_TO_FAV_SUCCESS))
        )
    }

    fun addToFavourite_error() {
        `when`(
            movieDbRepository.addToFavourite(
                any(MovieEntity::class.java)
            )
        ).thenThrow(
            RuntimeException(DB_TRANSACTION_ERROR)
        )
    }

    fun removeFromFavourite_success() {
        `when`(
            movieDbRepository.removeFromFavourite(
                any(MovieEntity::class.java)
            )
        ).thenReturn(
            Observable.just(Optional.of(REMOVE_FROM_FAV_SUCCESS))
        )
    }

    fun removeFromFavourite_error() {
        `when`(
            movieDbRepository.removeFromFavourite(
                any(MovieEntity::class.java)
            )
        ).thenThrow(
            RuntimeException(DB_TRANSACTION_ERROR)
        )
    }

    fun getAllFavourite_success_withData() {
        `when`(
            movieDbRepository.getAllFavourites()
        ).thenReturn(
            Observable.just(Optional.of(movieEntityList))
        )
    }

    fun getAllFavourite_success_withNoData() {
        `when`(
            movieDbRepository.getAllFavourites()
        ).thenReturn(
            Observable.just(Optional.of(mutableListOf()))
        )
    }

    fun getAllFavourite_error() {
        `when`(
            movieDbRepository.getAllFavourites()
        ).thenThrow(
            RuntimeException(DB_TRANSACTION_ERROR)
        )
    }

    fun getFavMovieById_success() {
        `when`(
            movieDbRepository.getFavouriteMovieById(any(Int::class.java))
        )
            .thenReturn(
                Observable.just(Optional.of(MOVIE_ENTITY))
            )
    }

    fun getFavMovieById_success_noData() {
        `when`(
            movieDbRepository.getFavouriteMovieById(any(Int::class.java))
        )
            .thenReturn(
                Observable.just(Optional.of(null))
            )
    }

    fun getFavMovieById_error() {
        `when`(
            movieDbRepository.getFavouriteMovieById(any(Int::class.java))
        )
            .thenThrow(RuntimeException(DB_TRANSACTION_ERROR))
    }

    //endregion
}
