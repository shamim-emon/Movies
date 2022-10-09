package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.movies.common.ASyncTransformer
import bd.emon.movies.common.NETWORK_ERROR_DEFAULT
import bd.emon.movies.common.NO_DATA_ERR
import bd.emon.movies.entity.Genre
import bd.emon.movies.entity.Genres
import bd.emon.movies.entity.Optional
import bd.emon.movies.rest.MovieApis
import bd.emon.movies.usecase.GetGenresUseCase
import io.reactivex.rxjava3.core.Observable
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    val API_KEY = "api_key"
    val LANG = "lang"
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

    lateinit var movieApisTd: MovieApisTd

    lateinit var getGenresUseCase: GetGenresUseCase

    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        movieApisTd = MovieApisTd()
        getGenresUseCase = GetGenresUseCase(ASyncTransformer(), movieApisTd)
        homeViewModel = HomeViewModel(getGenresUseCase)
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
        assertThat(movieApisTd.apiKey, `is`(API_KEY))
        assertThat(movieApisTd.lang, `is`(LANG))
    }

    @Test
    fun loadGenres_success_genreListReturned() {
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value!! == genreList, `is`(true))
    }

    @Test
    fun loadGenres_success_emptyListNoDataMessageReturned() {
        getGenres_noData()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.errorState.value!!.message == NO_DATA_ERR, `is`(true))
    }

    @Test
    fun loadGenres_success_emptyListReturned() {
        getGenres_noData()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value, nullValue())
    }

    @Test
    fun loadGenres_networkError_errorReturned() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.errorState.value!!.message, `is`(NETWORK_ERROR_DEFAULT))
    }

    @Test
    fun loadGenres_networkError_emptyListReturned() {
        getGenres_networkErr()
        homeViewModel.loadGenres(API_KEY, LANG)
        assertThat(homeViewModel.genres.value, nullValue())
    }

    //region helper methods
    fun getGenres_success() {
        movieApisTd.status = MovieApisTd.Companion.API_STATUS.SUCCESS
    }

    fun getGenres_networkErr() {
        movieApisTd.status = MovieApisTd.Companion.API_STATUS.ERROR
    }

    fun getGenres_noData() {
        movieApisTd.status = MovieApisTd.Companion.API_STATUS.NODATA
    }
    //endregion

    //region helper classes
    class MovieApisTd : MovieApis {

        companion object {
            enum class API_STATUS {
                SUCCESS, ERROR, NODATA
            }
        }

        var status: API_STATUS = API_STATUS.SUCCESS
        var apiKey: String? = ""
        var lang: String? = ""
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

        override fun getGenres(apiKey: String, language: String): Observable<Optional<Genres>> {
            this.apiKey = apiKey
            this.lang = language
            when (status) {
                API_STATUS.SUCCESS -> return Observable.just(Optional.of(genreList))
                API_STATUS.NODATA -> return Observable.just(Optional.empty())
                API_STATUS.ERROR -> throw Throwable(NETWORK_ERROR_DEFAULT)
                else -> throw IllegalStateException("${this.javaClass} has Invalid api status")
            }
        }
    }
    //endregion
}
