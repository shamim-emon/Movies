package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.movies.RxImmediateSchedulerRule
import bd.emon.movies.capture
import bd.emon.movies.common.ASyncTransformer
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.common.PARAM_API_KEY
import bd.emon.movies.common.PARAM_GENRES
import bd.emon.movies.common.PARAM_INCLUDE_ADULT
import bd.emon.movies.common.PARAM_LANGUAGE
import bd.emon.movies.common.PARAM_PAGE
import bd.emon.movies.common.PARAM_SORT_BY
import bd.emon.movies.common.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.movies.entity.Optional
import bd.emon.movies.fakeData.MovieApiDummyDataProvider
import bd.emon.movies.rest.MovieRepository
import bd.emon.movies.usecase.GetDiscoverMoviesUseCase
import bd.emon.movies.usecase.GetGenresUseCase
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
import org.mockito.Mockito.anyMap
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()
    val API_KEY = "api_key"
    val LANG = "lang"
    val SORT_BY = "sort_by"
    val INCLUDE_ADULT = true
    val PAGE = 11
    val VOTE_COUNT_GREATER_THAN = 10000
    val GENRE = 28

    @Captor
    lateinit var mapCaptor: ArgumentCaptor<HashMap<String, Any?>>

    @Mock
    lateinit var movieRepository: MovieRepository
    lateinit var getGenresUseCase: GetGenresUseCase
    lateinit var getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        getGenresUseCase = GetGenresUseCase(ASyncTransformer(), movieRepository)
        getDiscoverMoviesUseCase = GetDiscoverMoviesUseCase(ASyncTransformer(), movieRepository)
        homeViewModel = HomeViewModel(
            getGenresUseCase = getGenresUseCase,
            getDiscoverMoviesUseCase = getDiscoverMoviesUseCase
        )
        getGenres_success()
        getDiscoverMovies_success()
    }

    @Test
    fun loadGenres_rightParamsPassedToUseCase() {
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(getGenresUseCase.params?.get(PARAM_API_KEY), `is`(API_KEY))
        assertThat(getGenresUseCase.params?.get(PARAM_LANGUAGE), `is`(LANG))
    }

    @Test
    fun loadGenres_rightParamsPassedToRepository() {
        homeViewModel.loadGenres(API_KEY, LANG)
        verify(movieRepository, times(1)).getGenres(capture(mapCaptor))
        assertThat(mapCaptor.value[PARAM_API_KEY], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE], `is`(LANG))
    }

    @Test
    fun loadGenres_success_genreListEmitted() {
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value!! == MovieApiDummyDataProvider.genreList, `is`(true))
    }

    @Test
    fun loadGenres_success_loadStateFalseEmitted() {
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.loadingState.value!! == false, `is`(true))
    }

    @Test
    fun loadGenres_success_emptyListNoDataMessageEmitted() {
        getGenres_noData()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.errorState.value!!.message == NO_DATA_ERR, `is`(true))
    }

    @Test
    fun loadGenres_success_emptyListEmitted() {
        getGenres_noData()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value, nullValue())
    }

    @Test
    fun loadGenres_networkError_errorEmitted() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.errorState.value!!.message, `is`(NETWORK_ERROR_DEFAULT))
    }

    @Test
    fun loadGenres_networkError_LoadStateFalseEmitted() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.loadingState.value == false, `is`(true))
    }

    @Test
    fun loadGenres_networkError_emptyListEmitted() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value, nullValue())
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(apiKey = API_KEY, lang = LANG, genres = GENRE)
        assertThat(getDiscoverMoviesUseCase.params[PARAM_API_KEY], `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_LANGUAGE], `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_GENRES], `is`(GENRE))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY
        )
        assertThat(getDiscoverMoviesUseCase.params[PARAM_API_KEY], `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_LANGUAGE], `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_GENRES], `is`(GENRE))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_SORT_BY], `is`(SORT_BY))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_includeAdult_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT
        )
        assertThat(getDiscoverMoviesUseCase.params[PARAM_API_KEY], `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_LANGUAGE], `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_GENRES], `is`(GENRE))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_SORT_BY], `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_INCLUDE_ADULT], `is`(INCLUDE_ADULT))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_includeAdult_page_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE
        )
        assertThat(getDiscoverMoviesUseCase.params[PARAM_API_KEY], `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_LANGUAGE], `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_GENRES], `is`(GENRE))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_SORT_BY], `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_INCLUDE_ADULT], `is`(INCLUDE_ADULT))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_PAGE], `is`(PAGE))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_includeAdult_page_voteCountGreaterThan_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN
        )
        assertThat(getDiscoverMoviesUseCase.params[PARAM_API_KEY], `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_LANGUAGE], `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_GENRES], `is`(GENRE))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_SORT_BY], `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_INCLUDE_ADULT], `is`(INCLUDE_ADULT))
        assertThat(getDiscoverMoviesUseCase.params[PARAM_PAGE], `is`(PAGE))
        assertThat(
            getDiscoverMoviesUseCase.params[PARAM_VOTE_COUNT_GREATER_THAN],
            `is`(VOTE_COUNT_GREATER_THAN)
        )
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_passedToRepository() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE
        )
        verify(movieRepository, times(1)).getDiscoverMovies(
            capture(mapCaptor),
        )
        assertThat(mapCaptor.value[PARAM_API_KEY], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES], `is`(GENRE))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_passedToRepository() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY
        )
        verify(movieRepository, times(1)).getDiscoverMovies(
            capture(mapCaptor),
        )
        assertThat(mapCaptor.value[PARAM_API_KEY], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES], `is`(GENRE))
        assertThat(mapCaptor.value[PARAM_SORT_BY], `is`(SORT_BY))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_includeAdult_passedToRepository() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT
        )
        verify(movieRepository, times(1)).getDiscoverMovies(
            capture(mapCaptor),
        )
        assertThat(mapCaptor.value[PARAM_API_KEY], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES], `is`(GENRE))
        assertThat(mapCaptor.value[PARAM_SORT_BY], `is`(SORT_BY))
        assertThat(mapCaptor.value[PARAM_INCLUDE_ADULT], `is`(INCLUDE_ADULT))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_includeAdult_page_passedToRepository() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE
        )
        verify(movieRepository, times(1)).getDiscoverMovies(
            capture(mapCaptor),
        )
        assertThat(mapCaptor.value[PARAM_API_KEY], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES], `is`(GENRE))
        assertThat(mapCaptor.value[PARAM_SORT_BY], `is`(SORT_BY))
        assertThat(mapCaptor.value[PARAM_INCLUDE_ADULT], `is`(INCLUDE_ADULT))
        assertThat(mapCaptor.value[PARAM_PAGE], `is`(PAGE))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_genre_sortBy_includeAdult_page_voteCountGreaterThan_passedToRepository() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            genres = GENRE,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN
        )
        verify(movieRepository, times(1)).getDiscoverMovies(
            capture(mapCaptor),
        )
        assertThat(mapCaptor.value[PARAM_API_KEY], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES], `is`(GENRE))
        assertThat(mapCaptor.value[PARAM_SORT_BY], `is`(SORT_BY))
        assertThat(mapCaptor.value[PARAM_INCLUDE_ADULT], `is`(INCLUDE_ADULT))
        assertThat(mapCaptor.value[PARAM_PAGE], `is`(PAGE))
        assertThat(mapCaptor.value[PARAM_VOTE_COUNT_GREATER_THAN], `is`(VOTE_COUNT_GREATER_THAN))
    }

    @Test
    fun loadDiscoverMovies_success_listEmitted() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )
        assertThat(
            homeViewModel.discoverMovies.value,
            `is`(MovieApiDummyDataProvider.disocoverMovies)
        )
    }

    @Test
    fun loadDiscoverMovies_success_correct_grp_gen_idEmitted() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )
        assertThat(homeViewModel.discoverMovies.value!!.grp_genre_id, `is`(GENRE))
    }

    @Test
    fun loadDiscoverMovies_success_loadStateFalseEmitted() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )
        assertThat(
            homeViewModel.loadingState.value,
            `is`(false)
        )
    }

    @Test
    fun loadDiscoverMovies_successEmptyList_nullEmitted() {
        getDiscoverMovies_noData()
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )
        assertThat(
            homeViewModel.discoverMovies.value,
            `is`(nullValue())
        )
    }

    @Test
    fun loadDiscoverMovies_successEmptyList_noDataErrorMessageEmitted() {
        getDiscoverMovies_noData()
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )

        assertThat(homeViewModel.errorState.value!!.message == NO_DATA_ERR, `is`(true))
    }

    @Test
    fun loadDiscoverMovies_successEmptyList_loadStateFalseEmitted() {
        getDiscoverMovies_noData()
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )

        assertThat(homeViewModel.loadingState.value, `is`(false))
    }

    @Test
    fun loadDiscoverMovies_networkError_nullEmitted() {
        getDiscoverMovies_networkError()
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )

        assertThat(homeViewModel.discoverMovies.value, `is`(nullValue()))
    }

    @Test
    fun loadDiscoverMovies_networkError_networkErrorMessageEmitted() {
        getDiscoverMovies_networkError()
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )

        assertThat(homeViewModel.errorState.value!!.message, `is`(NETWORK_ERROR_DEFAULT))
    }

    @Test
    fun loadDiscoverMovies_networkError_loadStateFalseEmitted() {
        getDiscoverMovies_networkError()
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE
        )
        assertThat(homeViewModel.loadingState.value, `is`(false))
    }

    //region helper methods
    fun getGenres_success() {
        `when`(movieRepository.getGenres(anyMap()))
            .thenReturn(Observable.just(Optional.of(MovieApiDummyDataProvider.genreList)))
    }

    fun getGenres_networkErr() {
        `when`(movieRepository.getGenres(anyMap()))
            .thenThrow(RuntimeException(NETWORK_ERROR_DEFAULT))
    }

    fun getGenres_noData() {
        `when`(movieRepository.getGenres(anyMap()))
            .thenReturn(Observable.just(Optional.empty()))
    }

    fun getDiscoverMovies_success() {
        `when`(
            movieRepository.getDiscoverMovies(anyMap())
        )
            .thenReturn(Observable.just(Optional.of(MovieApiDummyDataProvider.disocoverMovies)))
    }

    fun getDiscoverMovies_noData() {
        `when`(movieRepository.getDiscoverMovies(anyMap()))
            .thenReturn(Observable.just(Optional.empty()))
    }

    fun getDiscoverMovies_networkError() {
        `when`(movieRepository.getDiscoverMovies(anyMap())).thenThrow(
            RuntimeException(
                NETWORK_ERROR_DEFAULT
            )
        )
    }
    //endregion
}
