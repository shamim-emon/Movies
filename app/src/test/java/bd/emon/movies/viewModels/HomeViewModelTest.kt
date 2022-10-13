package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.movies.any
import bd.emon.movies.capture
import bd.emon.movies.common.ASyncTransformer
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.entity.Optional
import bd.emon.movies.entity.genre.Genre
import bd.emon.movies.entity.genre.Genres
import bd.emon.movies.rest.MovieApis
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    val API_KEY = "api_key"
    val LANG = "lang"
    val SORT_BY = "sort_by"
    val INCLUDE_ADULT = true
    val PAGE = 1
    val VOTE_COUNT_GREATER_THAN = 10000
    val GENRE_SINGULAR = "28"
    val GENRE_MULTIPLE = "12,16,28"

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>

    @Mock
    lateinit var movieApis: MovieApis
    lateinit var getGenresUseCase: GetGenresUseCase
    lateinit var getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        getGenresUseCase = GetGenresUseCase(ASyncTransformer(), movieApis)
        getDiscoverMoviesUseCase = GetDiscoverMoviesUseCase(ASyncTransformer(), movieApis)
        homeViewModel = HomeViewModel(
            getGenresUseCase = getGenresUseCase,
            getDiscoverMoviesUseCase = getDiscoverMoviesUseCase
        )
        getGenres_success()
    }

    @Test
    fun loadGenres_rightParamsPassedToUseCase() {
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(getGenresUseCase.apiKey, `is`(API_KEY))
        assertThat(getGenresUseCase.lang, `is`(LANG))
    }

    @Test
    fun loadGenres_rightParamsPassedToApi() {
        homeViewModel.loadGenres(API_KEY, LANG)
        verify(movieApis, times(1)).getGenres(capture(stringCaptor), capture(stringCaptor))
        assertThat(stringCaptor.allValues[0], `is`(API_KEY))
        assertThat(stringCaptor.allValues[1], `is`(LANG))
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

    @Test(expected = Throwable::class)
    fun loadGenres_networkError_errorEmitted() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.errorState.value!!.message, `is`(NETWORK_ERROR_DEFAULT))
    }

    @Test(expected = Throwable::class)
    fun loadGenres_networkError_LoadStateFalseEmitted() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.loadingState.value == false, `is`(true))
    }

    @Test(expected = Throwable::class)
    fun loadGenres_networkError_emptyListEmitted() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value, nullValue())
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(apiKey = API_KEY, lang = LANG)
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(apiKey = API_KEY, lang = LANG, sortBy = SORT_BY)
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.sortBy, `is`(SORT_BY))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT
        )
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.sortBy, `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.includeAdult, `is`(INCLUDE_ADULT))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE
        )
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.sortBy, `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.includeAdult, `is`(INCLUDE_ADULT))
        assertThat(getDiscoverMoviesUseCase.page, `is`(PAGE))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_voteCountGreaterThan_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN
        )
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.sortBy, `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.includeAdult, `is`(INCLUDE_ADULT))
        assertThat(getDiscoverMoviesUseCase.page, `is`(PAGE))
        assertThat(getDiscoverMoviesUseCase.vote_count_greater_than, `is`(VOTE_COUNT_GREATER_THAN))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_voteCountGreaterThan_singleGenre_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE_SINGULAR
        )
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.sortBy, `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.includeAdult, `is`(INCLUDE_ADULT))
        assertThat(getDiscoverMoviesUseCase.page, `is`(PAGE))
        assertThat(getDiscoverMoviesUseCase.vote_count_greater_than, `is`(VOTE_COUNT_GREATER_THAN))
        assertThat(getDiscoverMoviesUseCase.genres, `is`(GENRE_SINGULAR))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_voteCountGreaterThan_multipleGenre_passedToUseCase() {
        homeViewModel.loadDiscoverMovies(
            apiKey = API_KEY,
            lang = LANG,
            sortBy = SORT_BY,
            includeAdult = INCLUDE_ADULT,
            page = PAGE,
            voteCountGreaterThan = VOTE_COUNT_GREATER_THAN,
            genres = GENRE_MULTIPLE
        )
        assertThat(getDiscoverMoviesUseCase.apiKey, `is`(API_KEY))
        assertThat(getDiscoverMoviesUseCase.lang, `is`(LANG))
        assertThat(getDiscoverMoviesUseCase.sortBy, `is`(SORT_BY))
        assertThat(getDiscoverMoviesUseCase.includeAdult, `is`(INCLUDE_ADULT))
        assertThat(getDiscoverMoviesUseCase.page, `is`(PAGE))
        assertThat(getDiscoverMoviesUseCase.vote_count_greater_than, `is`(VOTE_COUNT_GREATER_THAN))
        assertThat(getDiscoverMoviesUseCase.genres, `is`(GENRE_MULTIPLE))
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_voteCountGreaterThan_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_voteCountGreaterThan_singleGenre_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_correctParams_apiKey_language_sortBy_includeAdult_page_voteCountGreaterThan_multipleGenre_passedToApi() {
    }

    @Test
    fun loadDiscoverMovies_success_listEmitted() {
    }

    @Test
    fun loadDiscoverMovies_success_loadStateFalseEmitted() {
    }

    @Test
    fun loadDiscoverMovies_successEmptyList_nullEmitted() {
    }

    @Test
    fun loadDiscoverMovies_successEmptyList_noDataErrorMessageEmitted() {
    }

    @Test
    fun loadDiscoverMovies_successEmptyList_loadStateFalseEmitted() {
    }

    @Test
    fun loadDiscoverMovies_networkError_nullEmitted() {
    }

    @Test
    fun loadDiscoverMovies_networkError_networkErrorMessageEmitted() {
    }

    @Test
    fun loadDiscoverMovies_networkError_loadStateFalseEmitted() {
    }

    //region helper methods
    fun getGenres_success() {
        `when`(movieApis.getGenres(any(String::class.java), any(String::class.java)))
            .thenReturn(Observable.just(Optional.of(MovieApiDummyDataProvider.genreList)))
    }

    fun getGenres_networkErr() {
        `when`(movieApis.getGenres(any(String::class.java), any(String::class.java)))
            .thenThrow(Throwable(NETWORK_ERROR_DEFAULT))
    }

    fun getGenres_noData() {
        `when`(movieApis.getGenres(any(String::class.java), any(String::class.java)))
            .thenReturn(Observable.just(Optional.empty()))
    }
    //endregion

    object MovieApiDummyDataProvider {
        val genreList = Genres(
            genres = listOf(
                Genre(
                    id = 28,
                    name = "Action"
                ),
                Genre(
                    id = 12,
                    name = "Adventure"
                )
            )
        )
    }
    //endregion
}
