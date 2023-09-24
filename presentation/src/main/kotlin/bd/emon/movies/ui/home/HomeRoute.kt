package bd.emon.movies.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.hilt.navigation.compose.hiltViewModel
import bd.emon.data.dataMapper.DiscoverMovieMapper
import bd.emon.domain.PARAM_GENRES
import bd.emon.domain.entity.common.MovieEntity
import bd.emon.movies.viewModels.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeRoute(
    discoverMovieMapper: DiscoverMovieMapper
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val loadState by viewModel.loadingState.observeAsState()
    val genreErrorState by viewModel.genreErrorState.observeAsState()
    val genres by viewModel.genres.observeAsState()
    val pullRefreshState: SwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = loadState ?: false)
    val movieMap: SnapshotStateMap<Int, List<MovieEntity>> = remember { mutableStateMapOf() }
    viewModel.discoverMovies.observeAsState().value?.let {
        movieMap[it.grp_genre_id] = discoverMovieMapper.mapFrom(it.results)
    }

    val loadGenres :()->Unit = {
        viewModel.genreErrorState.postValue(null)
        viewModel.loadGenres(viewModel.apiParams)
    }

    HomeScreen(
        loadState = loadState ?: false,
        genreErrorState = genreErrorState,
        genres = genres,
        pullRefreshState = pullRefreshState,
        loadGenres = loadGenres,
        loadDiscoverMoviesByGenreId = {
            viewModel.apiParams[PARAM_GENRES] = it.toInt()
            viewModel.loadDiscoverMovies(viewModel.apiParams, 1)
        },
        movieMap = movieMap
    )

    loadGenres()
}
