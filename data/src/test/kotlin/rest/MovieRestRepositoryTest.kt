package rest

import TestScheduleProvider
import any
import bd.emon.data.rest.MovieRestApiInterface
import bd.emon.data.rest.MovieRestRepositoryImpl
import bd.emon.data.toApiParam
import bd.emon.domain.DEBOUNCE_DEAULT_DURATION
import bd.emon.domain.MovieRestRepository
import bd.emon.domain.PARAM_API_KEY
import bd.emon.domain.PARAM_GENRES
import bd.emon.domain.PARAM_INCLUDE_ADULT
import bd.emon.domain.PARAM_LANGUAGE
import bd.emon.domain.PARAM_PAGE
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
import bd.emon.domain.RELEASE_YEAR
import bd.emon.domain.entity.Optional
import bd.emon.movies.fakeData.MovieApiDummyDataProvider
import capture
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class MovieRestRepositoryTest {

    val MOVIE_ID = "movie_id"
    val API_KEY = "API_KEY"
    val LANG = "LANGUAGE"
    val SORT_BY = "sort_by"
    val INCLUDE_ADULT = false
    val PAGE = 11
    val VOTE_COUNT_GREATER_THAN = 10000
    val GENRE = 28

    @Mock
    lateinit var movieRestApiInterface: MovieRestApiInterface
    lateinit var movieRestRepository: MovieRestRepository

    @Captor
    lateinit var mapCaptor: ArgumentCaptor<Map<String, String>>

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>

    @Captor
    lateinit var intCaptor: ArgumentCaptor<Int>

    @Captor
    lateinit var boolCaptor: ArgumentCaptor<Boolean>

    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler {
            testScheduler
        }
        movieRestRepository = MovieRestRepositoryImpl(
            movieRestApiInterface,
            TestScheduleProvider(testScheduler)
        )
        getGenresApiResponse()
        getDiscoverMoviesApiResponse()
        getSearchResult_default()
        getMovieDetails_success()
        getMovieVideos_success()
    }

    @Test
    fun getGenres_correctParametersPassedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        movieRestRepository.getGenres(params)
        verify(movieRestApiInterface, times(1))
            .getGenres(
                capture(mapCaptor)
            )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
    }

    @Test
    fun getGenres_correctResponseReturned() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        movieRestRepository.getGenres(params).subscribe {
            assertThat(it, `is`(`is`(Optional.of(MovieApiDummyDataProvider.genreList))))
        }

        val scheduler = TestScheduler()
        scheduler.triggerActions()
    }

    @Test
    fun getDiscoverMovies_correctParams_apiKey_lang_genre_passedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        params[PARAM_GENRES] = GENRE
        movieRestRepository.getDiscoverMovies(params)
        verify(movieRestApiInterface, times(1)).getDiscoverMovies(
            capture(mapCaptor)
        )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES.toApiParam()], `is`("$GENRE"))
    }

    @Test
    fun getDiscoverMovies_correctParams_apiKey_lang_genre_sortBy_passedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        params[PARAM_GENRES] = GENRE
        params[PARAM_SORT_BY] = SORT_BY
        movieRestRepository.getDiscoverMovies(params)
        verify(movieRestApiInterface, times(1)).getDiscoverMovies(
            capture(mapCaptor)
        )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES.toApiParam()], `is`("$GENRE"))
        assertThat(mapCaptor.value[PARAM_SORT_BY.toApiParam()], `is`(SORT_BY))
    }

    @Test
    fun getDiscoverMovies_correctParams_apiKey_lang_genre_sortBy_includeAdult_passedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        params[PARAM_GENRES] = GENRE
        params[PARAM_SORT_BY] = SORT_BY
        params[PARAM_INCLUDE_ADULT] = INCLUDE_ADULT
        movieRestRepository.getDiscoverMovies(params)
        verify(movieRestApiInterface, times(1)).getDiscoverMovies(
            capture(mapCaptor)
        )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES.toApiParam()], `is`("$GENRE"))
        assertThat(mapCaptor.value[PARAM_SORT_BY.toApiParam()], `is`(SORT_BY))
        assertThat(
            mapCaptor.value[PARAM_INCLUDE_ADULT.toApiParam()],
            `is`(INCLUDE_ADULT.toString())
        )
    }

    @Test
    fun getDiscoverMovies_correctParams_apiKey_lang_genre_sortBy_includeAdult_page_passedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        params[PARAM_GENRES] = GENRE
        params[PARAM_SORT_BY] = SORT_BY
        params[PARAM_INCLUDE_ADULT] = INCLUDE_ADULT
        params[PARAM_PAGE] = PAGE
        movieRestRepository.getDiscoverMovies(params)
        verify(movieRestApiInterface, times(1)).getDiscoverMovies(
            capture(mapCaptor)
        )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES.toApiParam()], `is`("$GENRE"))
        assertThat(mapCaptor.value[PARAM_SORT_BY.toApiParam()], `is`(SORT_BY))
        assertThat(
            mapCaptor.value[PARAM_INCLUDE_ADULT.toApiParam()],
            `is`(INCLUDE_ADULT.toString())
        )
        assertThat(mapCaptor.value[PARAM_PAGE.toApiParam()], `is`(PAGE.toString()))
    }

    @Test
    fun getDiscoverMovies_correctParams_apiKey_lang_genre_sortBy_includeAdult_page_voteCountGreaterThan_passedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        params[PARAM_GENRES] = GENRE
        params[PARAM_SORT_BY] = SORT_BY
        params[PARAM_INCLUDE_ADULT] = INCLUDE_ADULT
        params[PARAM_PAGE] = PAGE
        params[PARAM_VOTE_COUNT_GREATER_THAN] = VOTE_COUNT_GREATER_THAN
        movieRestRepository.getDiscoverMovies(params)
        verify(movieRestApiInterface, times(1)).getDiscoverMovies(
            capture(mapCaptor)
        )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES.toApiParam()], `is`("$GENRE"))
        assertThat(mapCaptor.value[PARAM_SORT_BY.toApiParam()], `is`(SORT_BY))
        assertThat(
            mapCaptor.value[PARAM_INCLUDE_ADULT.toApiParam()],
            `is`(INCLUDE_ADULT.toString())
        )
        assertThat(mapCaptor.value[PARAM_PAGE.toApiParam()], `is`(PAGE.toString()))
        assertThat(
            mapCaptor.value[PARAM_VOTE_COUNT_GREATER_THAN.toApiParam()],
            `is`(VOTE_COUNT_GREATER_THAN.toString())
        )
    }

    @Test
    fun getDiscoverMovies_correctParams_apiKey_lang_genre_sortBy_includeAdult_page_voteCountGreaterThan_releaseYear_passedToApi() {
        val params = hashMapOf<String, Any?>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_LANGUAGE] = LANG
        params[PARAM_GENRES] = GENRE
        params[PARAM_SORT_BY] = SORT_BY
        params[PARAM_INCLUDE_ADULT] = INCLUDE_ADULT
        params[PARAM_PAGE] = PAGE
        params[PARAM_VOTE_COUNT_GREATER_THAN] = VOTE_COUNT_GREATER_THAN
        params[PARAM_RELEASE_YEAR] = RELEASE_YEAR
        movieRestRepository.getDiscoverMovies(params)
        verify(movieRestApiInterface, times(1)).getDiscoverMovies(
            capture(mapCaptor)
        )
        assertThat(mapCaptor.value[PARAM_API_KEY.toApiParam()], `is`(API_KEY))
        assertThat(mapCaptor.value[PARAM_LANGUAGE.toApiParam()], `is`(LANG))
        assertThat(mapCaptor.value[PARAM_GENRES.toApiParam()], `is`("$GENRE"))
        assertThat(mapCaptor.value[PARAM_SORT_BY.toApiParam()], `is`(SORT_BY))
        assertThat(
            mapCaptor.value[PARAM_INCLUDE_ADULT.toApiParam()],
            `is`(INCLUDE_ADULT.toString())
        )
        assertThat(mapCaptor.value[PARAM_PAGE.toApiParam()], `is`(PAGE.toString()))
        assertThat(
            mapCaptor.value[PARAM_VOTE_COUNT_GREATER_THAN.toApiParam()],
            `is`(VOTE_COUNT_GREATER_THAN.toString())
        )
        assertThat(mapCaptor.value[PARAM_RELEASE_YEAR.toApiParam()], `is`(RELEASE_YEAR))
    }

    @Test
    fun getSearchResult_correctParamsPassedToApi() {
        val query = "Some query"
        movieRestRepository.getSearchResult(
            API_KEY,
            LANG,
            PAGE,
            INCLUDE_ADULT,
            query
        ).test()
        testScheduler.advanceTimeBy(DEBOUNCE_DEAULT_DURATION, TimeUnit.MILLISECONDS)
        verify(movieRestApiInterface, times(1))
            .getSearchResult(
                capture(stringCaptor),
                capture(stringCaptor),
                capture(intCaptor),
                capture(boolCaptor),
                capture(stringCaptor)
            )
        assertThat(stringCaptor.allValues[0], `is`(API_KEY))
        assertThat(stringCaptor.allValues[1], `is`(LANG))
        assertThat(intCaptor.allValues[0], `is`(PAGE))
        assertThat(boolCaptor.allValues[0], `is`(INCLUDE_ADULT))
        assertThat(stringCaptor.allValues[2], `is`(query))
    }

    @Test
    fun getMovieDetails_correctParamsPassedToApi() {
        movieRestRepository.getMovieDetails(API_KEY, LANG, MOVIE_ID)
        verify(
            movieRestApiInterface,
            times(1)
        ).getMovieDetails(
            capture(stringCaptor),
            capture(stringCaptor),
            capture(stringCaptor)
        )

        assertThat(stringCaptor.allValues[0], `is`(MOVIE_ID))
        assertThat(stringCaptor.allValues[1], `is`(API_KEY))
        assertThat(stringCaptor.allValues[2], `is`(LANG))
    }

    @Test
    fun getMovieVideos_correctParamsPassedToApi() {
        movieRestRepository.getMovieVideos(MOVIE_ID, API_KEY)
        verify(
            movieRestApiInterface,
            times(1)
        ).getMovieVideos(
            capture(stringCaptor),
            capture(stringCaptor)
        )

        assertThat(stringCaptor.allValues[0], `is`(MOVIE_ID))
        assertThat(stringCaptor.allValues[1], `is`(API_KEY))
    }

    //region helper methods
    private fun getGenresApiResponse() {
        `when`(movieRestApiInterface.getGenres(anyMap()))
            .thenReturn(Observable.just((MovieApiDummyDataProvider.genreList)))
    }

    private fun getDiscoverMoviesApiResponse() {
        `when`(
            movieRestApiInterface.getDiscoverMovies(
                anyMap()
            )
        ).thenReturn(Observable.just(MovieApiDummyDataProvider.disocoverMovies))
    }

    private fun getSearchResult_default() {
        `when`(
            movieRestApiInterface.getSearchResult(
                any(String::class.java),
                any(String::class.java),
                any(Int::class.java),
                any(Boolean::class.java),
                any(String::class.java)
            )
        ).thenReturn(Observable.just(MovieApiDummyDataProvider.searchResults))
    }

    private fun getMovieDetails_success() {
        `when`(
            movieRestApiInterface.getMovieDetails(
                any(String::class.java),
                any(String::class.java),
                any(String::class.java)
            )
        ).thenReturn(
            Observable.just(
                MovieApiDummyDataProvider.movieDetails
            )
        )
    }

    private fun getMovieVideos_success() {
        `when`(
            movieRestApiInterface.getMovieVideos(
                any(String::class.java),
                any(String::class.java)
            )
        ).thenReturn(
            Observable.just(
                MovieApiDummyDataProvider.movieVideos
            )
        )
    }
    //endregion
}
