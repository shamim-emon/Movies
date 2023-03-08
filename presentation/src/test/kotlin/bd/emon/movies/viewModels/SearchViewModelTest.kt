package bd.emon.movies.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import bd.emon.domain.MovieRestRepository
import bd.emon.domain.NETWORK_ERROR_DEFAULT
import bd.emon.domain.NO_DATA_ERR
import bd.emon.domain.entity.Optional
import bd.emon.domain.usecase.GetSearchResultUseCase
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
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @Mock
    lateinit var movieRestRepository: MovieRestRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()
    val API_KEY = "api_key"
    val LANG = "lang"
    val INCLUDE_ADULT = true
    val PAGE = 11

    lateinit var getSearchResultUseCase: GetSearchResultUseCase
    lateinit var searchViewModel: SearchViewModel

    @Captor
    lateinit var stringCaptor: ArgumentCaptor<String>

    @Captor
    lateinit var intCaptor: ArgumentCaptor<Int>

    @Captor
    lateinit var booleanCaptor: ArgumentCaptor<Boolean>

    @Before
    fun setUp() {
        getSearchResultUseCase = GetSearchResultUseCase(movieRestRepository)
        searchViewModel = SearchViewModel(getSearchResultUseCase)
        getSearchResult_success()
    }

    @Test
    fun searchMovie_correctParamsPassedToUseCase() {
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(getSearchResultUseCase.apiKey, `is`(API_KEY))
        assertThat(getSearchResultUseCase.language, `is`(LANG))
        assertThat(getSearchResultUseCase.page, `is`(PAGE))
        assertThat(getSearchResultUseCase.includeAdult, `is`(INCLUDE_ADULT))
        assertThat(getSearchResultUseCase.query, `is`(query))
    }

    @Test
    fun searchMovie_correctParamsPassedToRepository() {
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )

        verify(movieRestRepository, times(1))
            .getSearchResult(
                capture(stringCaptor),
                capture(stringCaptor),
                capture(intCaptor),
                capture(booleanCaptor),
                capture(stringCaptor)
            )

        assertThat(stringCaptor.allValues[0], `is`(API_KEY))
        assertThat(stringCaptor.allValues[1], `is`(LANG))
        assertThat(intCaptor.allValues[0], `is`(PAGE))
        assertThat(booleanCaptor.allValues[0], `is`(INCLUDE_ADULT))
        assertThat(stringCaptor.allValues[2], `is`(query))
    }

    @Test
    fun searchMovie_success_searchResultEmitted() {
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.movieSearch.value, `is`(MovieApiDummyDataProvider.searchResults))
    }

    @Test
    fun searchMovie_success_loadStateFalseEmitted() {
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.loadingState.value, `is`(false))
    }

    @Test
    fun searchMovie_queryLessthanThreeCharRepositoryMethodNotCalled() {
        val query = "So"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        verify(movieRestRepository, never())
            .getSearchResult(
                capture(stringCaptor),
                capture(stringCaptor),
                capture(intCaptor),
                capture(booleanCaptor),
                capture(stringCaptor)
            )
    }

    @Test
    fun searchMovie_successEmptyResponse_noDataMessageEmitted() {
        getSearchResult_successEmptyResponse()
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.errorState.value!!.message, `is`(NO_DATA_ERR))
    }

    @Test
    fun searchMovie_successEmptyResponse_nullEmitted() {
        getSearchResult_successEmptyResponse()
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.movieSearch.value, `is`(nullValue()))
    }

    @Test
    fun searchMovie_successEmptyResponse_loadStateFalseEmitted() {
        getSearchResult_successEmptyResponse()
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.loadingState.value, `is`(false))
    }

    @Test
    fun searchMovie_networkError_nullEmitted() {
        getSearchResult_networkError()
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.movieSearch.value, `is`(nullValue()))
    }

    @Test
    fun searchMovie_networkError_networkErrorMessageEmitted() {
        getSearchResult_networkError()
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.errorState.value!!.message, `is`(NETWORK_ERROR_DEFAULT))
    }

    @Test
    fun searchMovie_networkError_loadStateFalseEmitted() {
        getSearchResult_networkError()
        val query = "Some query"
        searchViewModel.searchMovie(
            apiKey = API_KEY,
            language = LANG,
            page = PAGE,
            includeAdult = INCLUDE_ADULT,
            query = query
        )
        assertThat(searchViewModel.loadingState.value!!, `is`(false))
    }

    //region helper methods
    fun getSearchResult_success() {
        `when`(
            movieRestRepository.getSearchResult(
                any(String::class.java),
                any(String::class.java),
                any(Int::class.java),
                any(Boolean::class.java),
                any(String::class.java)
            )
        ).thenReturn(
            Observable.just(
                Optional.of(MovieApiDummyDataProvider.searchResults)
            )
        )
    }

    fun getSearchResult_successEmptyResponse() {
        `when`(
            movieRestRepository.getSearchResult(
                any(String::class.java),
                any(String::class.java),
                any(Int::class.java),
                any(Boolean::class.java),
                any(String::class.java)
            )
        ).thenReturn(Observable.just(Optional.empty()))
    }

    fun getSearchResult_networkError() {
        `when`(
            movieRestRepository.getSearchResult(
                any(String::class.java),
                any(String::class.java),
                any(Int::class.java),
                any(Boolean::class.java),
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
