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
import bd.emon.domain.PARAM_RELEASE_YEAR
import bd.emon.domain.PARAM_SORT_BY
import bd.emon.domain.PARAM_VOTE_COUNT_GREATER_THAN
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
    val filterMap: SnapshotStateMap<String, Any?> = remember { mutableStateMapOf() }
    viewModel.loadDiscoverFilters.observeAsState().value?.let {
        filterMap[PARAM_SORT_BY] = viewModel.apiParams[PARAM_SORT_BY] as String
        filterMap[PARAM_VOTE_COUNT_GREATER_THAN] =
            viewModel.apiParams[PARAM_VOTE_COUNT_GREATER_THAN]
        filterMap[PARAM_RELEASE_YEAR] = viewModel.apiParams[PARAM_RELEASE_YEAR]
    }
    val genreErrorState by viewModel.genreErrorState.observeAsState()
    val filtersErrorState by viewModel.discoverFiltersErrorState.observeAsState()
    val genres by viewModel.genres.observeAsState()
    val pullRefreshState: SwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = loadState ?: false)
    val movieMap: SnapshotStateMap<Int, List<MovieEntity>> = remember { mutableStateMapOf() }
    viewModel.discoverMovies.observeAsState().value?.let {
        movieMap[it.grp_genre_id] = discoverMovieMapper.mapFrom(it.results)
    }

    val loadGenres: () -> Unit = {
        viewModel.genreErrorState.postValue(null)
        viewModel.loadGenres(viewModel.apiParams)
    }

    val clearFilters: () -> Unit = {
        viewModel.clearFilterParams()
        viewModel.genreErrorState.postValue(null)
        viewModel.loadGenres(viewModel.apiParams)
    }

    var isInitialComposition by remember { mutableStateOf(true) }

    val movieReleaseYears = movieReleaseYearsProvider.getReleaseYears()

    viewModel.saveDiscoverFilters.observeAsState().value.let {
        viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()
        viewModel.genreErrorState.postValue(null)
        viewModel.loadGenres(viewModel.apiParams)
    }

    HomeScreen(
        loadState = loadState ?: false,
        genreErrorState = genreErrorState,
        saveFilterErrorState = filtersErrorState,
        genres = genres,
        pullRefreshState = pullRefreshState,
        loadGenres = loadGenres,
        loadDiscoverMoviesByGenreId = {
            viewModel.apiParams[PARAM_GENRES] = it.toInt()
            viewModel.loadDiscoverMovies(viewModel.apiParams, 1)
        },
        clearFilters = clearFilters,
        clearFilterErrorState = { viewModel.discoverFiltersErrorState.postValue(null) },
        updateFilters = viewModel::saveDiscoverMoviesFilters,
        movieMap = movieMap,
        movieReleaseYears = movieReleaseYears,
        filters = filterMap
    )

    if (isInitialComposition) {
        isInitialComposition = false
        viewModel.loadDiscoverMovieFiltersAndHoldInApiParamMap()
        loadGenres()
    }
}
