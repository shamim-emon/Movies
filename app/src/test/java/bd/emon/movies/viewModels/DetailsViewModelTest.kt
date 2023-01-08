package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.movies.any
import bd.emon.movies.capture
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.entity.Optional
import bd.emon.movies.fakeData.MovieApiDummyDataProvider
import bd.emon.movies.rest.MovieRestRepository
import bd.emon.movies.usecase.GetMovieDetailsUseCase
import io.reactivex.rxjava3.core.Observable
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
class DetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    val API_KEY = "api_key"
    val LANG = "lang"
    val MOVIE_ID = "movie_id"

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>

    @Mock
    lateinit var movieRestRepository: MovieRestRepository
    lateinit var detailsViewModel: DetailsViewModel
    lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @Before
    fun setUp() {
        getMovieDetailsUseCase = GetMovieDetailsUseCase(movieRestRepository)
        detailsViewModel = DetailsViewModel(getMovieDetailsUseCase)
        getMovieDetails_success()
    }

    @Test
    fun getMovieDetails_correctparams_passed_to_useCase() {
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        assertThat(getMovieDetailsUseCase.apiKey, `is`(API_KEY))
        assertThat(getMovieDetailsUseCase.language, `is`(LANG))
        assertThat(getMovieDetailsUseCase.movieId, `is`(MOVIE_ID))
    }

    @Test
    fun getMovieDetails_correctparams_passed_to_repository() {
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        verify(movieRestRepository, times(1))
            .getMovieDetails(
                capture(stringCaptor),
                capture(stringCaptor),
                capture(stringCaptor)
            )

        assertThat(stringCaptor.allValues[0], `is`(API_KEY))
        assertThat(stringCaptor.allValues[1], `is`(LANG))
        assertThat(stringCaptor.allValues[2], `is`(MOVIE_ID))
    }

    @Test
    fun getMovieDetail_success_movieDetailsEmitted() {
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        assertThat(
            detailsViewModel.movieDetails.value,
            `is`(MovieApiDummyDataProvider.movieDetails)
        )
    }

    @Test
    fun getMovieDetail_success_loadStateFalseEmitted() {
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        assertThat(detailsViewModel.loadingState.value, `is`(false))
    }

    @Test
    fun getMovieDetail_networkError_nullEmitted() {
        getMovieDetails_networkError()
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        assertThat(detailsViewModel.movieDetails.value, `is`(nullValue()))
    }

    @Test
    fun getMovieDetail_networkError_networkErrorMessageEmitted() {
        getMovieDetails_networkError()
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        assertThat(detailsViewModel.errorState.value!!.message, `is`(NETWORK_ERROR_DEFAULT))
    }

    @Test
    fun getMovieDetail_networkError_loadStateFalseEmitted() {
        getMovieDetails_networkError()
        detailsViewModel.getMovieDetails(
            apiKey = API_KEY,
            language = LANG,
            movieId = MOVIE_ID
        )
        assertThat(detailsViewModel.loadingState.value, `is`(false))
    }

    //region helper methods
    fun getMovieDetails_success() {
        `when`(
            movieRestRepository.getMovieDetails(
                any(String::class.java),
                any(String::class.java),
                any(String::class.java)
            )
        ).thenReturn(
            Observable.just(Optional.of(MovieApiDummyDataProvider.movieDetails))
        )
    }

    fun getMovieDetails_networkError() {
        `when`(
            movieRestRepository.getMovieDetails(
                any(String::class.java),
                any(String::class.java),
                any(String::class.java)
            )
        ).thenThrow(
            RuntimeException(
                NETWORK_ERROR_DEFAULT
            )
        )
    }
    //endregion
}
