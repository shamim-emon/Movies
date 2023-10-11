package bd.emon.movies.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.hilt.navigation.compose.hiltViewModel
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.domain.PARAM_GENRES
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.domain.entity.discover.DiscoverMovies
import bd.emon.domain.entity.genre.Genres
import bd.emon.movies.home.MovieReleaseYearsProvider
import bd.emon.movies.viewModels.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeRoute(
    discoverMovieMapper: DiscoverMovieMapper,
    movieReleaseYearsProvider: MovieReleaseYearsProvider
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val loadState by viewModel.loadingState.observeAsState(initial = true)
    val genreErrorState by viewModel.genreErrorState.observeAsState(initial = null)
    val filtersErrorState by viewModel.discoverFiltersErrorState.observeAsState(initial = null)
    val genres by viewModel.genres.observeAsState(initial = Genres(listOf()))
    val movies by viewModel.discoverMovies.observeAsState(
        DiscoverMovies(
            0,
            mutableListOf(),
            0,
            0,
            0
        )
    )
    val movieMap: SnapshotStateMap<Int, List<MovieEntity>> = remember { mutableStateMapOf() }

    val pullRefreshState: SwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = loadState)
    var isInitialComposition by remember { mutableStateOf(true) }

    val movieReleaseYears = movieReleaseYearsProvider.getReleaseYears()

    val loadGenres: () -> Unit = {
        viewModel.genreErrorState.postValue(null)
        viewModel.genres.postValue(Genres(listOf()))
        viewModel.loadGenres(viewModel.apiParams)
    }

    val loadDiscoverMoviesByGenreId: (String) -> Unit = {
        viewModel.apiParams[PARAM_GENRES] = it.toInt()
        viewModel.loadDiscoverMovies(viewModel.apiParams, 1)
    }

    val loadOrReloadHomeScreen: () -> Unit = {
        viewModel.discoverMovies.postValue(DiscoverMovies(0, mutableListOf(), 0, 0, 0))
        movieMap.clear()
        viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()
        loadGenres()
    }

    val clearFilters: () -> Unit = {
        viewModel.clearFilterParams()
        loadOrReloadHomeScreen()
    }

    val updateFilters: (Int, Boolean, String, String) -> Unit =
        { minVoteCount: Int, showAdult: Boolean, orderBy: String, releaseYr: String ->
            viewModel.saveDiscoverMoviesFilters(
                minVoteCount = minVoteCount,
                includeAdultContent = showAdult,
                orderBy = orderBy,
                releaseYearStr = releaseYr
            )
            loadOrReloadHomeScreen()
        }

    val clearFiltersError: () -> Unit = {
        viewModel.discoverFiltersErrorState.postValue(null)
    }

    val addToMovieMap: (DiscoverMovies) -> Unit = { movieGrp ->
        movieMap[movieGrp.grp_genre_id] = discoverMovieMapper.mapFrom(movieGrp.results).toList()
    }

    if (isInitialComposition) {
        isInitialComposition = false
        loadOrReloadHomeScreen()
    }

    HomeScreen(
        loadState = loadState,
        genreErrorState = genreErrorState,
        saveFilterErrorState = filtersErrorState,
        genres = genres,
        pullRefreshState = pullRefreshState,
        loadOrReloadHomeScree = loadOrReloadHomeScreen,
        loadDiscoverMoviesByGenreId = loadDiscoverMoviesByGenreId,
        clearFilters = clearFilters,
        clearFilterErrorState = clearFiltersError,
        updateFilters = updateFilters,
        movies = movies,
        movieReleaseYears = movieReleaseYears,
        filters = viewModel.apiParams,
        movieMap = movieMap,
        addToMovieMap = addToMovieMap
    )
}