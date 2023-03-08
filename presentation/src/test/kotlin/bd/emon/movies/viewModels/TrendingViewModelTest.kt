package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.domain.MovieRestRepository
import bd.emon.domain.NETWORK_ERROR_DEFAULT
import bd.emon.domain.NO_DATA_ERR
import bd.emon.domain.entity.Optional
import bd.emon.domain.usecase.GetTrendingMoviesUseCase
import bd.emon.movies.any
import bd.emon.movies.capture
import bd.emon.movies.fakeData.MovieApiDummyDataProvider
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
class TrendingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val API_KEY = "api_key"
    val PAGE = 11

    @Captor
    lateinit var intingCaptor: ArgumentCaptor<Int>

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>

    @Mock
    lateinit var movieRestRepository: MovieRestRepository
    lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    lateinit var trendingViewModel: TrendingViewModel

    @Before
    fun setUp() {
        getTrendingMoviesUseCase = GetTrendingMoviesUseCase(
            movieRestRepository
        )
        trendingViewModel =
            TrendingViewModel(apiKey = API_KEY, getTrendingMoviesUseCase = getTrendingMoviesUseCase)

        getTrendingMovies_success()
    }

    @Test
    fun loadTrendingMovies_correctParams_passedToUseCase() {
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(
            getTrendingMoviesUseCase.apiKey,
            `is`(API_KEY)
        )
        assertThat(
            getTrendingMoviesUseCase.page,
            `is`(PAGE)
        )
    }

    @Test
    fun loadTrendingMovies_correctParams_passedToRepository() {
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        verify(movieRestRepository, times(1)).getTrendingMovies(
            capture(stringCaptor),
            capture(intingCaptor)
        )
        assertThat(stringCaptor.value, `is`(API_KEY))
        assertThat(intingCaptor.value, `is`(PAGE))
    }

    @Test
    fun loadTrendingMovies_success_trendingListEmitted() {
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(
            trendingViewModel.trendingMovies.value,
            `is`(MovieApiDummyDataProvider.trendingMovies)
        )
    }

    @Test
    fun loadTrendingMovies_successEmptyResponse_nullEmitted() {
        getTrendingMovies_noData()
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(
            trendingViewModel.trendingMovies.value,
            `is`(nullValue())
        )
    }

    @Test
    fun loadTrendingMovies_successEmptyResponse_NoDataMessageEmitted() {
        getTrendingMovies_noData()
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(trendingViewModel.trendingMoviesErrorState.value!!.message, `is`(NO_DATA_ERR))
    }

    @Test
    fun loadTrendingMovies_successEmptyResponse_loadingStateFaliseEmitted() {
        getTrendingMovies_noData()
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(trendingViewModel.loadingState.value, `is`(false))
    }

    @Test
    fun loadTrendingMovies_networkError_networkErrorMessageEmitted() {
        getTrendingMovies_networkError()
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(
            trendingViewModel.trendingMoviesErrorState.value!!.message,
            `is`(NETWORK_ERROR_DEFAULT)
        )
    }

    @Test
    fun loadTrendingMovies_networkError_nullEmitted() {
        getTrendingMovies_networkError()
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(trendingViewModel.trendingMovies.value, `is`(nullValue()))
    }

    @Test
    fun loadTrendingMovies_networkError_loadingStateFalseEmitted() {
        getTrendingMovies_networkError()
        trendingViewModel.loadTrendingMovies(API_KEY, PAGE)
        assertThat(trendingViewModel.loadingState.value, `is`(false))
    }

    //region helper methods
    fun getTrendingMovies_success() {
        `when`(
            movieRestRepository.getTrendingMovies(
                any(String::class.java),
                any(Int::class.java)
            )
        )
            .thenReturn(Observable.just(Optional.of(MovieApiDummyDataProvider.trendingMovies)))
    }

    fun getTrendingMovies_noData() {
        `when`(
            movieRestRepository.getTrendingMovies(
                any(String::class.java),
                any(Int::class.java)
            )
        )
            .thenReturn(
                Observable.just(Optional.empty())
            )
    }

    fun getTrendingMovies_networkError() {
        `when`(
            movieRestRepository.getTrendingMovies(
                any(String::class.java),
                any(Int::class.java)
            )
        )
            .thenThrow(
                RuntimeException(NETWORK_ERROR_DEFAULT)
            )
    }
    //endregion helper method
}
