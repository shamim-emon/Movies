package bd.emon.movies.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    val loadState by viewModel.loadingState.observeAsState()
    val genreErrorState by viewModel.genreErrorState.observeAsState()
    val filtersErrorState by viewModel.discoverFiltersErrorState.observeAsState()
    val genres by viewModel.genres.observeAsState()

    val pullRefreshState: SwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = loadState ?: false)
    var isInitialComposition by remember { mutableStateOf(true) }
    val movieMap: SnapshotStateMap<Int, List<MovieEntity>> = remember { mutableStateMapOf() }
    val movieReleaseYears = movieReleaseYearsProvider.getReleaseYears()

    val loadGenres: () -> Unit = {
        viewModel.genreErrorState.postValue(null)
        viewModel.loadGenres(viewModel.apiParams)
    }

    val loadDiscoverMoviesByGenreId: (String) -> Unit = {
        viewModel.apiParams[PARAM_GENRES] = it.toInt()
        viewModel.loadDiscoverMovies(viewModel.apiParams, 1)
    }

    val loadOrReloadHomeScreen: () -> Unit = {
        viewModel.genres.postValue(null)
        movieMap.clear()
        viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()
        loadGenres()
    }

    val clearFilters: () -> Unit = {
        viewModel.clearFilterParams()
        loadOrReloadHomeScreen()
    }

    val clearFiltersError: () -> Unit = {
        viewModel.discoverFiltersErrorState.postValue(null)
    }

    viewModel.saveDiscoverFilters.observeAsState().value.let {
        LaunchedEffect(key1 = it) {
            loadOrReloadHomeScreen()
        }
    }

    viewModel.discoverMovies.observeAsState().value?.let { item ->
        movieMap[item.grp_genre_id] = discoverMovieMapper.mapFrom(item.results)
    }

    if (isInitialComposition) {
        isInitialComposition = false
        loadOrReloadHomeScreen()
    }

    HomeScreen(
        loadState = loadState ?: false,
        genreErrorState = genreErrorState,
        saveFilterErrorState = filtersErrorState,
        genres = genres,
        pullRefreshState = pullRefreshState,
        loadOrReloadHomeScree = loadOrReloadHomeScreen,
        loadDiscoverMoviesByGenreId = loadDiscoverMoviesByGenreId,
        clearFilters = clearFilters,
        clearFilterErrorState = clearFiltersError,
        updateFilters = viewModel::saveDiscoverMoviesFilters,
        movieMap = movieMap,
        movieReleaseYears = movieReleaseYears,
        filters = viewModel.apiParams
    )
}